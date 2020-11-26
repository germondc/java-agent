package test.clyde;

import java.lang.instrument.Instrumentation;

import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.matcher.ElementMatchers;

public class ByteBuddyAgent2 {
	public static void premain(final String agentArgs, final Instrumentation inst) throws Exception {
		System.out.printf("Starting %s\n", ByteBuddyAgent2.class.getSimpleName());

		new AgentBuilder.Default().type(ElementMatchers.any()).transform(new MetricsTransformer())
				//.with(AgentBuilder.Listener.StreamWriting.toSystemOut())
				.with(AgentBuilder.TypeStrategy.Default.REDEFINE).installOn(inst);
	}
}
