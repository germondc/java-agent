package test.clyde;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;

public class TimingTransformer implements ClassFileTransformer {
	public TimingTransformer(String args) {
	}

	@Override
	public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
			ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {

		byte[] byteCode = classfileBuffer;
		if (((className.startsWith("alchemy/")) || (className.startsWith("com/standardbank/")))
				&& (classBeingRedefined == null)) {
			try {
				ClassPool cp = ClassPool.getDefault();
				CtClass cc = cp.get("SamplePrinter");
				CtMethod m = cc.getDeclaredMethod("printLine");

				m.addLocalVariable("profilerStartTime", CtClass.longType);
				m.insertBefore("profilerStartTime = System.nanoTime();");
				m.insertAfter(
						"long profilerEndTime = System.nanoTime(); TimingOutput.addLine(profilerStartTime, profilerEndTime, Thread.currentThread());");
				byteCode = cc.toBytecode();
				cc.detach();
			} catch (Exception ex) {
				ex.printStackTrace();
				throw new IllegalClassFormatException("An error occurred on formatting class");
			}
		}
		return byteCode;
	}
}
