package ww.midnite.jcatgui9kw;

import ww.midnite.jcatgui9kw.engine.Controller;
import ww.midnite.util.Helper;


public class Main {

	public Main() {
		Helper.initLaf();
	}


	public void start(final String[] args) {
		Globals.loadProfile(args.length == 0 ? "" : args[0]);

		new Controller().start();
	}

}
