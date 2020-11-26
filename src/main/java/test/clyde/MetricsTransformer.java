package test.clyde;

import java.lang.reflect.Executable;

import net.bytebuddy.agent.builder.AgentBuilder.Transformer;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.asm.AsmVisitorWrapper;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.matcher.ElementMatchers;

public class MetricsTransformer implements Transformer {
	@Override
	public DynamicType.Builder<?> transform(final DynamicType.Builder<?> builder, final TypeDescription typeDescription,
			final ClassLoader classLoader) {

		final AsmVisitorWrapper methodsVisitor = Advice.to(EnterAdvice.class, ExitAdviceMethods.class)
				.on(ElementMatchers.isMethod());

		final AsmVisitorWrapper constructorsVisitor = Advice.to(EnterAdvice.class, ExitAdviceConstructors.class)
				.on(ElementMatchers.isConstructor());

		return builder.visit(methodsVisitor).visit(constructorsVisitor);
	}

	private static class EnterAdvice {
		@Advice.OnMethodEnter
		static long enter() {
			System.out.println("EnterAdvice");
			return System.nanoTime();
		}
	}

	private static class ExitAdviceMethods {
		@Advice.OnMethodExit(onThrowable = Throwable.class)
		static void exit(@Advice.Origin final Executable executable, @Advice.Enter final long startTime) {
			final long duration = System.nanoTime() - startTime;
			MetricsCollector.report(executable.toGenericString(), duration);
			System.out.println("ExitAdviceMethods");
		}
	}

	private static class ExitAdviceConstructors {
		@Advice.OnMethodExit
		static void exit(@Advice.Origin final Executable executable, @Advice.Enter final long startTime) {
			final long duration = System.nanoTime() - startTime;
			MetricsCollector.report(executable.toGenericString(), duration);
			System.out.println("ExitAdviceConstructors");
		}
	}
}
