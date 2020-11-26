package test.clyde;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class ClassPrinter extends ClassVisitor {

	public ClassPrinter(ClassWriter writer) {
		super(Opcodes.ASM7, writer);
	}

	private String className;

	@Override
	public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
		//System.out.println(name + " extends " + superName + " {");
		this.className = name;
		super.visit(version, access, name, signature, superName, interfaces);
	}

	@Override
	public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
		System.out.println(className + " " + name + "  " + desc);
		MethodVisitor mv = cv.visitMethod(access, name, desc, signature, exceptions);
		return mv == null ? null : new MethodPrinter(Opcodes.ASM7, mv);
	}

	@Override
	public void visitEnd() {
		//System.out.println("}");
		super.visitEnd();
	}
}