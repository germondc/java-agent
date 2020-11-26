package test.clyde;

import net.bytebuddy.asm.Advice;

public class TimerAdvice {
	@Advice.OnMethodEnter
	static long enter() {
		System.out.println("here");
		return System.currentTimeMillis();
	}

	@Advice.OnMethodExit(onThrowable = Throwable.class)
	static void exit(@Advice.Origin String method, @Advice.Enter long start) {
		System.out.println(method + " took " + (System.currentTimeMillis() - start));
	}
}