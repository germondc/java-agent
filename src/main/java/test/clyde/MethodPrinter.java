package test.clyde;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class MethodPrinter extends MethodVisitor implements Opcodes {

	public MethodPrinter(int api) {
		super(api);
	}

	public MethodPrinter(int api, MethodVisitor methodVisitor) {
		super(api, methodVisitor);
	}

	@Override
	public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
		/* System.err.println("CALL" + name); */
		mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
		mv.visitLdcInsn("  >> " + name + desc);
		mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);

		/* do call */
		mv.visitMethodInsn(opcode, owner, name, desc, itf);

		/* System.err.println("RETURN" + name);  */
		mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
		mv.visitLdcInsn("  << " + name + desc);
		mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
	}
}
