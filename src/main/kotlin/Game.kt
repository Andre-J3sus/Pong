import pt.isel.canvas.*


data class States(val playing: Boolean, val finished :Boolean, val menu :Boolean)
//Class Game que contém uma bola, uma raquete1 e uma raquete 2
data class Game(val ball: Ball, val p1: Player, val p2: Player, val states :States, val menu: Menu)

//Constantes de:
const val WIDTH = 600            //Comprimento do jogo
const val HEIGHT = 400           //Altura do jogo
const val GREY = 0x808080        //Cor cizenta(background da janela)
const val SCORE_X = WIDTH/2 - 27 //Coordenada X da pontuação
const val SCORE_Y = 40           //Coordenada Y da pontuação
const val SCORE_SIZE = 24        //Tamanho da fonte da pontuação


/**
 * Função que recebe um jogo e:
 *  - desenha a sua bola
 *  - desenha a raquete 1 (esquerda)
 *  - desenha a raquete 2 (direita)
 *  - desenha a pontuação
 */
fun Canvas.drawGame(game:Game){
    erase()
    if(game.states.menu){
        drawMenu(game.menu)
    }
    else{
        drawBall(game.ball)
        drawRacket(game.p1.bat)
        drawRacket(game.p2.bat)
        if (!game.states.finished){
            drawText(SCORE_X, SCORE_Y, "${game.p1.score} : ${game.p2.score}", BLACK, SCORE_SIZE)
            drawRect(SCORE_X - 2, SCORE_Y - SCORE_SIZE +2, SCORE_SIZE*2 + 12, SCORE_SIZE + 2, BLACK, 2)
        }
        else{
            if (game.p1.wins()) writeWins(1) else writeWins(2)
        }
    }
}


/**
 * Função que retorna um Game com as condições iniciais do jogo
 */
fun startConditions() = Game(
    Ball(Position(WIDTH/2, HEIGHT/2), Velocity(0, 0)),
    Player(Racket(Position(10, RACKET_INITIAL_Y)), 0),
    Player(Racket(Position(WIDTH - (RACKET_WIDTH + 10), RACKET_INITIAL_Y)), 0),
    States(
        playing = false,
        finished = false,
        menu = true),
    Menu(Buttom(Position(WIDTH/2, HEIGHT/2), "PLAY", false))
)


/**
 * Função que retorna um jogo em estado inicial com o score bem(sem estar a zeros, como nas startConditions)
 */
fun Game.startingGame() = startConditions().copy(
    p1=Player(startConditions().p1.bat, this.p1.score),
    p2=Player(startConditions().p2.bat, this.p2.score),
    states = states.copy(
        finished = this.states.finished,
        menu = this.states.menu
    )
)


/**
 * Função que verifica se a bola está parada e retorna um Jogo com a bola a movimentar-se
 */
fun Game.startPlaying() =
    Game(Ball(ball.pos, Velocity(dxRange.random(), dyRange.random())), p1, p2, states.copy(playing = true), menu)


/**
 * Função que retorna um Game com a bola movimentada
 */
fun Game.moveBall()= if (states.playing && !states.finished) Game(ball.move(this), p1, p2, states, menu) else this


/**
 * Função que verifica se a bola desapareceu, se sim, retorna um Jogo com as condições iniciais
 */
fun Game.checkGoal():Game{
    return when{
        ball.leaveByRight()-> {
            playSound("goal")
            Game(ball, p1.addScore(), p2, States(playing = false, finished = p1.addScore().wins(), states.menu), menu).startingGame()
        }
        ball.leaveByLeft()-> {
            playSound("goal")
            Game(ball, p1, p2.addScore(), States(playing = false, finished = p2.addScore().wins(), states.menu), menu).startingGame()
        }
        else -> this
    }
}
