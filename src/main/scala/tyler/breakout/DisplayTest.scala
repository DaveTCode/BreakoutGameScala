package tyler.breakout

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

class DisplayTest {
  /**
   * Creates a new DisplayTest
   */
  def DisplayTest() {
  }

  /**
   * Runs the tests
   */
  def executeTest() : Unit = {
    currentTest();
    queryModesTest();
    setDisplayModeTest();
    setDisplayConfigurationTest();
  }

  /**
   * Prints some info about the current mode
   */
  def currentTest() : Unit = {
    println("==== Test Current ====");

    println("Info about current:");
    println("Graphics card: " + Display.getAdapter() + ", version: " + Display.getVersion());
    println("Resolution: " +
        Display.getDisplayMode().getWidth()      + "x" +
        Display.getDisplayMode().getHeight()     + "x" +
        Display.getDisplayMode().getBitsPerPixel()      + "@" +
        Display.getDisplayMode().getFrequency()  + "Hz");
    println("---- Test Current ----");
  }

  /**
   * Tests querying for modes
   */
  def queryModesTest() : Unit = {
    var modes : Array[DisplayMode] = null;

    println("==== Test query ====");
    println("Retrieving available displaymodes");
    modes = Display.getAvailableDisplayModes();

    // no modes check
    if (modes == null) {
      println("FATAL: unable to find any modes!");
      System.exit(-1);
    }

    // write some info
    println("Found " + modes.length + " modes");
    println("The first 5 are:");
    modes.take(5).foreach(mode => {
      println(mode);
    });
    println("---- Test query ----");
  }


  /**
   * Tests setting display modes
   */
  def setDisplayModeTest() : Unit = {
    println("==== Test setDisplayMode ====");
    println("Retrieving available displaymodes");
    var modes = Display.getAvailableDisplayModes();

    // no modes check
    if (modes == null) {
      println("FATAL: unable to find any modes!");
      System.exit(-1);
    }

    // find a mode
    print("Looking for 640x480...");
	val mode = modes.filter(mode => (mode.getWidth() == 640) && (mode.getHeight() == 480)).first;

    // no mode check
    if (mode == null) {
      println("error\nFATAL: Unable to find basic mode.");
      System.exit(-1);
    }

    // change to mode, and wait a bit
    print("Changing to mode...");
    try {
      Display.setDisplayMode(mode);
      Display.setFullscreen(true);
      Display.create();
    } catch {
      case e : Exception => 
      	println("error\nFATAL: Error setting mode");
      	System.exit(-1);
    }
    println("done");

    println("Resolution: " +
        Display.getDisplayMode().getWidth()      + "x" +
        Display.getDisplayMode().getHeight()     + "x" +
        Display.getDisplayMode().getBitsPerPixel()      + "@" +
        Display.getDisplayMode().getFrequency()  + "Hz");

    pause(5000);

    // reset
    print("Resetting mode...");
    try {
        Display.setFullscreen(false);
    } catch {
      case e : LWJGLException =>
        e.printStackTrace();
    }
    println("done");

    println("---- Test setDisplayMode ----");
  }

  /**
   * Tests the DisplayConfiguration
   */
  def setDisplayConfigurationTest() : Unit = {
    println("==== Test setDisplayConfigurationTest ====");

    println("Testing normal setting");
    changeConfig(1.0f, 0f, 1f);

    println("Testing gamma settings");
    changeConfig(5.0f, 0f, 1f);
    changeConfig(0.5f, 0f, 1f);

    println("Testing brightness settings");
    changeConfig(1.0f, -1.0f, 1f);
    changeConfig(1.0f, -0.5f, 1f);
    changeConfig(1.0f, 0.5f, 1f);
    changeConfig(1.0f, 1.0f, 1f);

    println("Testing contrast settings");
    changeConfig(1.0f, 0f, 0f);
    changeConfig(1.0f, 0f, 0.5f);
    changeConfig(1.0f, 0f, 10000.0f);

    print("resetting...");
    try {
        Display.setFullscreen(false);
    } catch {
      case e : LWJGLException =>
        e.printStackTrace();
    }
    println("done");

    println("---- Test setDisplayConfigurationTest ----");
  }

  /**
   * Changes the Display configuration
   *
   * @param gamma gamma value to change to
   * @param brightness brightness value to change to
   * @param contrast contrast value to change to
   */
  def changeConfig(gamma : Float, brightness : Float, contrast : Float) : Unit = {
    try {
      Display.setDisplayConfiguration(gamma, brightness, contrast);
      println("Configuration changed, gamma = " + gamma + " brightness = " + brightness + " contrast = " + contrast);
    } catch {
      case e : Exception =>
      println("Failed on: gamma = " + gamma + " brightness = " + brightness + " contrast = " + contrast);
    }
    pause(3000);
  }

  /**
   * Pause current thread for a specified time
   *
   * @param time milliseconds to sleep
   */
  def pause(time : Integer) : Unit = {
	  val sleepDelay = 100;
	  for (i <- 0 until time by sleepDelay) {
	    try {
			  Display.processMessages();
			  Thread.sleep(sleepDelay);
		  } catch {
		    case e : InterruptedException => println("Thread interrupted...continuing");
		  }
	  }
  }
}