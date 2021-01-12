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
fun Racket.upLimit() = Racket(Position(pos.x, 0))
fun Racket.bottomLimit() = Racket(Position(pos.x, RACKET_LIMIT_Y))

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
        UP_CODE   -> this.copy(p2 = p2.copy(bat = if (p2.bat.nextY() <= 0) p2.bat.upLimit() else p2.bat.up()))

        DOWN_CODE -> this.copy(p2 = p2.copy(bat = if (p2.bat.nextBottomY() >= HEIGHT) p2.bat.bottomLimit() else p2.bat.down()))

        W_CODE    -> this.copy(p1 = p1.copy(bat = if (p1.bat.nextY() <= 0) p1.bat.upLimit() else p1.bat.up()))

        S_CODE    -> this.copy(p1 = p1.copy(bat = if (p1.bat.nextBottomY() >= HEIGHT) p1.bat.bottomLimit() else p1.bat.down()))

        else -> this
    }
}
