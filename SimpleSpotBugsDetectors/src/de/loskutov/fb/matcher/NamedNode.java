/*******************************************************************************
 * Copyright (c) 2013 Andrey Loskutov.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * Contributor:
 *   Andrey Loskutov
 *******************************************************************************/
package de.loskutov.fb.matcher;

import static org.objectweb.asm.tree.AbstractInsnNode.*;

import java.util.regex.Pattern;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.TypeInsnNode;

public abstract class NamedNode extends InstructionMatcher {

    /**
     * One of
     * <li> {@link #ANY_TYPE}
     * <li> {@link AbstractInsnNode#VAR_INSN}
     * <li> {@link AbstractInsnNode#FIELD_INSN}
     * <li> {@value AbstractInsnNode#METHOD_INSN}
     */
    public final static int ANY_TYPE = -1;
    public final static int ANY_OPCODE = -1;
    private final int type;
    private final String owner;
    protected final String name;
    private final String desc;

    protected NamedNode(int type, int opcode, final String owner, final String name,
            final String desc) {
        super();
        this.opcode = opcode;
        this.type = type;
        this.owner = owner;
        this.name = name;
        this.desc = desc;
    }

    @Override
    public boolean matches(AbstractInsnNode n) {
        if(type == ANY_TYPE) {
            if (n instanceof FieldInsnNode) {
                return matchesInt((FieldInsnNode) n);
            } else if (n instanceof MethodInsnNode) {
                return matchesInt((MethodInsnNode) n);
            }
        } else if (type == FIELD_INSN) {
            if (n instanceof FieldInsnNode) {
                return matchesInt((FieldInsnNode) n);
            }
        } else if(type == METHOD_INSN) {
            if (n instanceof MethodInsnNode) {
                return matchesInt((MethodInsnNode)n);
            }
        } else if(type == TYPE_INSN) {
            if (n instanceof TypeInsnNode) {
                return matchesInt((TypeInsnNode)n);
            }
        } /*else if(type == -1 || type == AbstractInsnNode.VAR_INSN) {
            if (n instanceof VarInsnNode) {
                VarInsnNode m1 = (VarInsnNode) n;
                return opcodeEquals(m1.getOpcode()) && nameEquals("TODO" + m1.var);
            }
        }*/
        return false;
    }

    private boolean matchesInt(TypeInsnNode node) {
        return opcodeEquals(node.getOpcode()) && equals(node.desc, desc);
    }

    private boolean matchesInt(FieldInsnNode node) {
        return opcodeEquals(node.getOpcode()) && nameEquals(node.name) && equals(node.owner, owner)
                && equals(node.desc, desc);
    }

    private boolean matchesInt(MethodInsnNode node) {
        return opcodeEquals(node.getOpcode()) && nameEquals(node.name) && equals(node.owner, owner)
                && equals(node.desc, desc);
    }

    protected boolean opcodeEquals(int other){
        return opcode == ANY_OPCODE || opcode == other;
    }

    abstract protected boolean nameEquals(String name1);

    abstract protected boolean equals(String name1, String name2);

    public static class Field extends NamedNode {
        public Field(int opcode, String owner, String name,
                String desc) {
            super(FIELD_INSN, opcode, owner, name, desc);
        }

        @Override
        protected boolean equals(String name1, String name2) {
            return nullOrEquals(name1, name2);
        }

        @Override
        protected boolean nameEquals(String name1) {
            return equals(name1, name);
        }
    }

    public static class Method extends NamedNode {
        public Method(int opcode, String owner, String name,
                String desc) {
            super(METHOD_INSN, opcode, owner, name, desc);
        }

        @Override
        protected boolean equals(String name1, String name2) {
            return nullOrEquals(name1, name2);
        }

        @Override
        protected boolean nameEquals(String name1) {
            return equals(name1, name);
        }
    }

    public static class New extends NamedNode {
        public New(String typeDescriptor) {
            super(TYPE_INSN, Opcodes.NEW, null, null, typeDescriptor);
        }

        @Override
        protected boolean equals(String name1, String name2) {
            return nullOrEquals(name1, name2);
        }

        @Override
        protected boolean nameEquals(String name1) {
            return equals(name1, name);
        }
    }

    public static class RegExField extends NamedNode {
        private final Pattern pattern;

        public RegExField(int opcode, String owner, String name,
                String desc) {
            super(FIELD_INSN, opcode, owner, name, desc);
            pattern = Pattern.compile(name, Pattern.CASE_INSENSITIVE);
        }

        @Override
        protected boolean equals(String name1, String name2) {
            return nullOrEquals(name1, name2);
        }

        @Override
        protected boolean nameEquals(String name1) {
            return pattern.matcher(name1).matches();
        }
    }

    public static class RegExMethod extends NamedNode {
        private final Pattern pattern;

        public RegExMethod(int opcode, String owner, String name,
                String desc) {
            super(METHOD_INSN, opcode, owner, name, desc);
            pattern = Pattern.compile(name, Pattern.CASE_INSENSITIVE);
        }

        @Override
        protected boolean equals(String name1, String name2) {
            return nullOrEquals(name1, name2);
        }

        @Override
        protected boolean nameEquals(String name1) {
            return pattern.matcher(name1).matches();
        }
    }

    public static class RegExNamedNode extends NamedNode {
        private final Pattern pattern;

        public RegExNamedNode(int opcode, String owner, String name,
                String desc) {
            super(ANY_TYPE, opcode, owner, name, desc);
            pattern = Pattern.compile(name, Pattern.CASE_INSENSITIVE);
        }

        @Override
        protected boolean equals(String name1, String name2) {
            return nullOrEquals(name1, name2);
        }

        @Override
        protected boolean nameEquals(String name1) {
            return pattern.matcher(name1).matches();
        }
    }
}
