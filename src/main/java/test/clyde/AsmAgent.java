package test.clyde;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class AsmAgent {

	public static void premain(String agentArgs, Instrumentation inst) {
		inst.addTransformer(new ClassFileTransformer() {
			@Override
			public byte[] transform(ClassLoader classLoader, String s, Class<?> aClass,
					ProtectionDomain protectionDomain, byte[] bytes) throws IllegalClassFormatException {

				if (s.startsWith("alchemy/trader") || s.startsWith("alchemy/measure")
						|| s.startsWith("com/standardbank/")) {
					// ASM Code
					ClassReader reader = new ClassReader(bytes);
					ClassWriter writer = new ClassWriter(reader, 0);
					//ClassPrinter visitor = new ClassPrinter(writer);
					ClassVisitor visitor = new ClassVisitor(Opcodes.ASM7, writer) {
						@Override
						public MethodVisitor visitMethod(int access, final String name, String desc, String signature,
								String[] exceptions) {
							MethodVisitor mv = cv.visitMethod(access, name, desc, signature, exceptions);
							MethodVisitor mv2 = new MethodVisitor(Opcodes.ASM7, mv) {
								@Override
								public void visitCode() {
									mv.visitMethodInsn(Opcodes.INVOKESTATIC, "test/clyde/Logger", "logIn", "()V");
								}

								@Override
								public void visitEnd() {
									mv.visitMethodInsn(Opcodes.INVOKESTATIC, "test/clyde/Logger", "logOut", "()V");
								}
							};

							return mv2;
						}
					};
					reader.accept(visitor, 0);
					return writer.toByteArray();

				}

				return null;
			}
		});
	}

}
