import pt.isel.canvas.*

data class Racket(val pos: Position)//Data class Racket que contém uma Posição

//Constantes de:
const val RACKET_WIDTH = 10 //Comprimento da raquete
const val RACKET_HEIGHT = 90 // Altura da raquete
const val RACKET_DY = 6 //Deslocamento Y da raquete
const val W_CODE = 87 //Código da tecla W
const val S_CODE = 83 //Código da tecla S
const val RACKET_LIMIT_Y = HEIGHT - RACKET_HEIGHT //Coordenada Y mais alta(limite inferior)
const val RACKET_INITIAL_Y = (HEIGHT-RACKET_HEIGHT)/2 //Coordenada Y inicial


//Funções que retornam a coordenada Y da raquete com o deslocamento:
fun Racket.up() = Racket(Position(pos.x, pos.y - RACKET_DY)) //subtraido - sobe
fun Racket.down() = Racket(Position(pos.x, pos.y + RACKET_DY)) //acrescentado - desce

fun Racket.rangeY() = pos.y..pos.y + RACKET_HEIGHT //Função que retorna o intervalo de valores Y da raquete
fun Racket.xLimit() = if (pos.x < WIDTH/2) pos.x + RACKET_WIDTH + BALL_RADIUS else pos.x - BALL_RADIUS
fun Racket.nextBottomY() = pos.y + RACKET_HEIGHT + RACKET_DY //Próxima coordenada Y da zona debaixo da raquete
fun Racket.nextY() = pos.y - RACKET_DY//Próxima coordenada Y da raquete


/**
 * Função que desenha a raquete
 */
fun Canvas.drawRacket(rk:Racket){
    drawRect(rk.pos.x, rk.pos.y, RACKET_WIDTH, RACKET_HEIGHT, BLACK)
}


/**
 * Função que retorna um Game com alguma das raquetes movimentadas.
 *  - A raquete esquerda(bat1) movimenta-se com W(sobe) e S(desce)
 *  - A raquete direita(bat2) movimenta-se com a seta para cima e a seta para baixo
 */
fun Game.moveRacket(ke: KeyEvent):Game{
    return when(ke.code){
        UP_CODE   -> {
            Game(ball, p1, Player(
                if (p2.bat.nextY() <= 0) Racket(Position(p2.bat.pos.x, 0)) else p2.bat.up(),
                p2.score), states)
        }

        DOWN_CODE -> {
            Game(ball, p1, Player(
                if (p2.bat.nextBottomY() >= HEIGHT) Racket(Position(p2.bat.pos.x, RACKET_LIMIT_Y)) else p2.bat.down(),
                p2.score), states)
        }

        W_CODE    -> {
            Game(ball, Player(
                if (p1.bat.nextY() <= 0) Racket(Position(p1.bat.pos.x, 0)) else p1.bat.up(),
                p2.score), p2, states)
        }

        S_CODE    -> {
            Game(ball, Player(
                if (p1.bat.nextBottomY() >= HEIGHT) Racket(Position(p1.bat.pos.x, RACKET_LIMIT_Y)) else p1.bat.down(),
                p2.score), p2, states)
        }
        else -> this
    }
}
