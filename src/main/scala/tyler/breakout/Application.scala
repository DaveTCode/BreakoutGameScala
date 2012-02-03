package tyler.breakout

object Application {
  /**
   * Tests the Sys class, and serves as basic usage test
   *
   * @param args ignored
   */
  def main(args: Array[String]) : Unit = {
    new DisplayTest().executeTest();
    System.exit(0);
  }
}