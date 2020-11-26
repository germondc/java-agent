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
		if ((className.startsWith("alchemy/trader") || className.startsWith("com/standardbank/")
				|| className.startsWith("alchemy/measure")) && classBeingRedefined == null) {

			//			if (className.contains("FastClassByGuice") || className.contains("EnhancerBySpringCGLIB"))
			//				return byteCode;
			try {
				ClassPool cp = ClassPool.getDefault();
				CtClass cc = cp.makeClass(new java.io.ByteArrayInputStream(byteCode));

				for (CtMethod method : cc.getDeclaredMethods()) {

					if (method.isEmpty() == false) {
						//String signature = JavassistHelper.getSignature(method);
						method.insertBefore("test.clyde.LoggingOutput.addStart(Thread.currentThread(), \""
								+ method.getLongName() + "\");");
						//						method.insertAfter("test.clyde.LoggingOutput.addEnd(Thread.currentThread(), \""
						//								+ method.getLongName() + "\");");
						method.insertAfter("System.out.println();");
					}
				}

				byteCode = cc.toBytecode();
				cc.detach();
			} catch (Exception ex) {
				ex.printStackTrace();
				throw new IllegalClassFormatException("An error occurred on formatting class");
			}
		}
		return byteCode;
	}

	//	/** classes to always not to instrument */
	//	static final String[] DEFAULT_EXCLUDES = new String[] { "com/sun/", "sun/", "java/", "javax/", "org/slf4j" };
	//
	//	/** only this classes should instrument or leave empty to instrument all classes that not excluded */
	//	static final String[] INCLUDES = new String[] { "alchemy/measure", "alchemy/trader" };
	//
	//	/**
	//	 * instrument class
	//	 */
	//	@Override
	//	public byte[] transform(final ClassLoader loader, final String className, final Class<?> clazz,
	//			final ProtectionDomain domain, final byte[] bytes) {
	//
	//		if (clazz != null)
	//			return bytes;
	//
	//		for (String include : INCLUDES) {
	//
	//			if (className.startsWith(include)) {
	//				return doClass(className, clazz, bytes);
	//			}
	//		}
	//
	//		return bytes;
	//	}
	//
	//	/**
	//	 * instrument class with javasisst
	//	 */
	//	private byte[] doClass(final String name, final Class<?> clazz, byte[] b) {
	//		ClassPool pool = ClassPool.getDefault();
	//		CtClass cl = null;
	//
	//		try {
	//			cl = pool.makeClass(new java.io.ByteArrayInputStream(b));
	//			if (cl.getName().contains("FastClassByGuice"))
	//				return b;
	//			if (cl.isInterface() == false) {
	//				CtBehavior[] methods = cl.getDeclaredBehaviors();
	//
	//				for (int i = 0; i < methods.length; i++) {
	//
	//					if (methods[i].isEmpty() == false) {
	//						doMethod(methods[i]);
	//					}
	//				}
	//
	//				b = cl.toBytecode();
	//			}
	//		} catch (Exception e) {
	//			System.err.println("Could not instrument  " + name + ",  exception : " + e.getMessage());
	//		} finally {
	//
	//			if (cl != null) {
	//				cl.detach();
	//			}
	//		}
	//
	//		return b;
	//	}
	//
	//	/**
	//	 * modify code and add log statements before the original method is called
	//	 * and after the original method was called
	//	 */
	//	private void doMethod(final CtBehavior method) throws NotFoundException, CannotCompileException {
	//		String signature = JavassistHelper.getSignature(method);
	//		String returnValue = JavassistHelper.returnValue(method);
	//
	//		method.insertBefore("System.out.println(\">> " + signature + "\");");
	//		method.insertAfter("System.out.println(\"<< " + signature + returnValue + "\");", true);
	//	}
}
