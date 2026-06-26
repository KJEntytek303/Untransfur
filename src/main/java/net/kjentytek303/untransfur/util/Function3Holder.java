package net.kjentytek303.untransfur.util;

import com.mojang.datafixers.util.Function3;


/**
 * Function3Holder is a Function3 wrapper that allows you not to specify arguments at declaration time.
 * Calling Function3Holder(fun3, a1, a2, a3).run() is equivalent to using Function3 directly.
 */

public class Function3Holder<Arg1, Arg2, Arg3, Ret> {

	public Function3Holder(Function3<Arg1, Arg2, Arg3, Ret> cfun) { this(cfun, null, null, null); }
	public Function3Holder(Function3<Arg1, Arg2, Arg3, Ret> cfun, Arg1 ca1) { this(cfun, ca1, null, null); }
	public Function3Holder(Function3<Arg1, Arg2, Arg3, Ret> cfun, Arg1 ca1, Arg2 ca2) { this(cfun, ca1, ca2, null); }

	public Function3Holder(Function3<Arg1, Arg2, Arg3, Ret> cfun, Arg1 ca1, Arg2 ca2, Arg3 ca3 ) {
		a1=ca1;
		a2=ca2;
		a3=ca3;
		fun = cfun;
	}

	public Arg1 a1;
	public Arg2 a2;
	public Arg3 a3;
	public Function3<Arg1, Arg2, Arg3, Ret> fun;
	public Ret apply() {
		return fun.apply(a1, a2, a3);
	}
}