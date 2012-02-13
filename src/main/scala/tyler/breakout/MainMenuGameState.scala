package tyler.breakout

import org.newdawn.slick.state.{StateBasedGame}
import de.lessvoid.nifty.Nifty
import de.lessvoid.nifty.slick2d.NiftyBasicGameState
import de.lessvoid.nifty.builder.{ImageBuilder, ScreenBuilder, LayerBuilder}

class MainMenuGameState(stateId: Int, game: Game) extends NiftyBasicGameState  {

  override def getID(): Int = stateId

  override def prepareNifty(nifty: Nifty, stateBasedGame: StateBasedGame) {
    val screen = new ScreenBuilder("start") {{
      controller(new MainMenuController(game))
      layer(new LayerBuilder("background") {{
        childLayoutCenter()

        image(new ImageBuilder("background-image") {{
          filename("/src/main/resources/main-menu-bg.png")
          width("800px")
          height("600px")
        }});
      }})

      layer (new LayerBuilder("buttons") {{
        childLayoutAbsolute()

        image(new ImageBuilder("new-game-button") {{
          x("300px")
          y("200px")
          filename("/src/main/resources/new-game-button.png")
          interactOnClick("newGame()")
          width("200px")
          height("100px")
        }})

        image(new ImageBuilder("exit-button") {{
          x("300px")
          y("300px")

          filename("/src/main/resources/exit-button.png")
          interactOnClick("exit()")
          width("200px")
          height("100px")
        }})
      }})
    }}.build(nifty)

  }
}