package test.clyde;

import java.lang.instrument.Instrumentation;

import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType.Builder;
import net.bytebuddy.matcher.ElementMatchers;

public class ByteBuddyAgent {

	public static void premain(String arguments, Instrumentation instrumentation) {
		//        new AgentBuilder.Default()
		//        .type(ElementMatchers.nameEndsWith("Timed"))
		//        .transform((builder, type, classLoader, module) -> {
		//            builder.visit(Advice.to(TimerAdvice).on(ElementMatchers.any()));
		//        });
		System.out.println("premain");
		new AgentBuilder.Default().type(ElementMatchers.any()).transform(new AgentBuilder.Transformer() {

			@Override
			public Builder<?> transform(Builder<?> builder, TypeDescription typeDescription, ClassLoader classLoader) {
				System.out.println("dying");
				return builder.visit(Advice.to(TimerAdvice.class).on(ElementMatchers.any()));
			}
		}).installOn(instrumentation);

		//		new AgentBuilder.Default().type(ElementMatchers.any())
		//		    .transform((builder, type) -> builder
		//		    .method(ElementMatchers.isAnnotatedBy(Secured.class)
		//		    .intercept(MethodDelegation.to(SecurityInterceptor.class)
		//		               .andThen(SuperMethodCall.INSTANCE))))
		//		    .installOn(inst);
	}

}
