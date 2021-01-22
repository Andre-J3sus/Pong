import pt.isel.canvas.*


//Class States com os estados do jogo
data class States(val playing: Boolean, val finished :Boolean, val menu :Boolean, val aiXPl :Boolean, val plXPl:Boolean)

//Class Game que contém uma bola, um jogador 1, um jogador 2, estados e menu
data class Game(val ball: Ball, val p1: Player, val p2: Player, val states :States, val menu: Menu)

//Constantes de:
const val WIDTH = 600            //Comprimento do jogo
const val HEIGHT = 400           //Altura do jogo
const val FIELD_GREEN = 0x228B22 //Cor cizenta(background da janela)
const val GOLD = 0xFFD700        //Cor dourada (título do jogo)
const val LINES_THICKNESS = 3    //Grossura das linhas do background
const val LINES_RADIUS = 7       //Raio das linhas do background
const val SCORE_X = WIDTH/2 - 34 //Coordenada X da pontuação
const val SCORE_Y = 40           //Coordenada Y da pontuação
const val SCORE_SIZE = 28        //Tamanho da fonte da pontuação


/**
 * Função que recebe um jogo e:
 *  - desenha as linhas do jogo
 *  - Se estivermos no menu, desenha o menu;
 *  - Senão:
 *      - desenha a sua bola
 *      - desenha a raquete 1 (esquerda)
 *      - desenha a raquete 2 (direita)
 *      - desenha a pontuação se o ogo estiver a decorrer
 *      - desenha o botão de voltar ao menu se o jogo tiver acabado
 *      - desenha quem foi o vencedor se o jogo tiver acabado
 */
fun Canvas.drawGame(game:Game){
    erase()
    drawLines()

    if(game.states.menu) drawMenu(game.menu)

    else{
        drawBall(game.ball)
        drawRacket(game.p1.bat, CYAN)
        drawRacket(game.p2.bat, RED)
        if (!game.states.finished) drawScore(game)
        else {
            drawButton(game.menu.menuButton, YELLOW, GOLD)
            if (game.p1.wins()) writeWins(1, CYAN)
            else writeWins(2, RED)
        }
    }
}


/**
 * Função que retorna um Game com as condições iniciais do jogo
 */
fun startConditions() = Game(
    Ball(Position(WIDTH/2, HEIGHT/2), Velocity(0, 0)),
    Player(Racket(Position(10, RACKET_INITIAL_Y), up = false, down = false), 0),
    Player(Racket(Position(WIDTH - (RACKET_WIDTH + 10), RACKET_INITIAL_Y), up = false, down = false), 0),
    States(
        playing = false,
        finished = false,
        menu = true,
        aiXPl = false,
        plXPl = false
    ),
    Menu(
        Button(Position(WIDTH/4, HEIGHT/2), " 1 Player", false),
        Button(Position(WIDTH*3/4, HEIGHT/2), "2 Players", false),
        Button(Position(WIDTH/2, HEIGHT/2), "  MENU", false)
    )
)


/**
 * Função que retorna um jogo em estado inicial com o score bem(sem estar a zeros, como nas startConditions)
 */
fun Game.startingGame() = startConditions().copy(
    p1=Player(startConditions().p1.bat, this.p1.score),
    p2=Player(startConditions().p2.bat, this.p2.score),
    states = states.copy(
        finished = this.states.finished,
        menu = this.states.menu,
        aiXPl = this.states.aiXPl,
        plXPl = this.states.plXPl,
    )
)


/**
 * Função que verifica se a bola está parada e retorna um Jogo com a bola a movimentar-se
 */
fun Game.startPlaying() =
    if(!states.playing && !ball.isMoving() && !states.menu)
        Game(Ball(ball.pos, Velocity(dxRange.random(), dyRange.random())), p1, p2, states.copy(playing = true), menu)
    else this


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
            Game(ball, p1.addScore(), p2, states.copy(playing = false, finished = p1.addScore().wins()), menu).startingGame()
        }
        ball.leaveByLeft()-> {
            playSound("goal")
            Game(ball, p1, p2.addScore(), states.copy(playing = false, finished = p2.addScore().wins()), menu).startingGame()
        }
        else -> this
    }
}


/**
 * Função que desenha as linhas do background do jogo
 */
fun Canvas.drawLines(){
    drawLine(WIDTH/2, 0, WIDTH/2, HEIGHT, WHITE, LINES_THICKNESS)
    drawCircle(WIDTH/2, HEIGHT/2, 30, WHITE, LINES_THICKNESS)
    drawCircle(WIDTH/2, HEIGHT/2, LINES_RADIUS, WHITE)

    drawArc(0, 0, LINES_RADIUS, -90, 0, WHITE, LINES_THICKNESS)
    drawArc(WIDTH, 0, LINES_RADIUS, 180, 270, WHITE, LINES_THICKNESS)
    drawArc(0, HEIGHT, LINES_RADIUS, 0, 90, WHITE, LINES_THICKNESS)
    drawArc(WIDTH, HEIGHT, LINES_RADIUS, 90, 180, WHITE, LINES_THICKNESS)

    drawLine(0, 0, WIDTH, 0, WHITE, LINES_THICKNESS)
    drawLine(0, 0, 0, HEIGHT, WHITE, LINES_THICKNESS)
    drawLine(0, HEIGHT, WIDTH, HEIGHT, WHITE, LINES_THICKNESS)
    drawLine(WIDTH, HEIGHT, WIDTH, 0, WHITE, LINES_THICKNESS)
}

/**
 * Função que desenha o score
 */
fun Canvas.drawScore(game:Game){
    drawText(SCORE_X, SCORE_Y, "${game.p1.score}", BLACK, SCORE_SIZE+1) //CYAN SCORE SHADOW
    drawText(SCORE_X, SCORE_Y, "${game.p1.score}", CYAN, SCORE_SIZE) //CYAN SCORE
    drawText(SCORE_X, SCORE_Y, "     ${game.p2.score}", BLACK, SCORE_SIZE+1) //RED SCORE SHADOW
    drawText(SCORE_X, SCORE_Y, "     ${game.p2.score}", RED, SCORE_SIZE) //RED SCORE
}
