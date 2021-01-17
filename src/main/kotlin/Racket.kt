import pt.isel.canvas.*

data class Racket(val pos: Position, val up:Boolean, val down:Boolean)//Data class Racket que contém uma Posição

//Constantes de:
const val RACKET_WIDTH = 10 //Comprimento da raquete
const val RACKET_HEIGHT = 90 // Altura da raquete
const val RACKET_DY = 4 //Deslocamento Y da raquete
const val W_CODE = 87 //Código da tecla W
const val S_CODE = 83 //Código da tecla S
const val RACKET_LIMIT_Y = HEIGHT - RACKET_HEIGHT //Coordenada Y mais alta(limite inferior)
const val RACKET_INITIAL_Y = (HEIGHT-RACKET_HEIGHT)/2 //Coordenada Y inicial


//Funções que retornam a coordenada Y da raquete com o deslocamento:
fun Racket.up() = copy(pos = Position(pos.x, pos.y - RACKET_DY)) //subtraido - sobe
fun Racket.down() = copy(pos = Position(pos.x, pos.y + RACKET_DY)) //acrescentado - desce

fun Racket.upLimit() = copy(pos = Position(pos.x, 0)) //Raquete na sua coordenada Y mínima
fun Racket.bottomLimit() = copy(pos = Position(pos.x, RACKET_LIMIT_Y)) //Raquete na sua coordenada Y maxima
fun Racket.rangeY() = pos.y..pos.y + RACKET_HEIGHT //Função que retorna o intervalo de valores Y da raquete
fun Racket.xLimit() = if (pos.x < WIDTH/2) pos.x + RACKET_WIDTH + BALL_RADIUS else pos.x - BALL_RADIUS
fun Racket.nextBottomY() = pos.y + RACKET_HEIGHT + RACKET_DY //Próxima coordenada Y debaixo da raquete
fun Racket.nextY() = pos.y - RACKET_DY //Próxima coordenada Y da raquete
fun Racket.centerY() = pos.y + RACKET_HEIGHT/2 //Coodenada Y do centro da raquete


/**
 * Função que desenha a raquete
 */
fun Canvas.drawRacket(rk:Racket){
    drawRect(rk.pos.x, rk.pos.y, RACKET_WIDTH, RACKET_HEIGHT, BLACK)
}


/**
 * Função que retorna um Game com a raquete direita(bat2) movimenta-se com a seta para cima e a seta para baixo
 */
fun Game.moveRacket1Player(ke: KeyEvent):Game{
    return when(ke.code){
        UP_CODE   -> this.copy(p2 = p2.copy(bat = if (p2.bat.nextY() <= 0) p2.bat.upLimit() else p2.bat.up()))

        DOWN_CODE -> this.copy(p2 = p2.copy(bat = if (p2.bat.nextBottomY() >= HEIGHT) p2.bat.bottomLimit() else p2.bat.down()))

        else -> this
    }
}


/**
 * Função que retorna um Game com as raquetes em movimento
 */
fun Game.moveRacket2Players():Game = copy(p1 = p1.copy(bat = p1.bat.moviment()), p2 = p2.copy(bat = p2.bat.moviment()))


fun Racket.moviment():Racket =
    when{
        up && nextY() >= 0              -> up()
        down && nextBottomY() <= HEIGHT -> down()
        else -> this
    }


/**
 * Função que verifica que key foi premida e retorna um Game com as propriedades das raquetes mudadas
 */
fun Game.checkKey(ke: KeyEvent):Game{
    return when(ke.code){
        UP_CODE   -> this.copy(p2 = p2.copy(bat = p2.bat.copy(up = true, down = false)))

        DOWN_CODE -> this.copy(p2 = p2.copy(bat = p2.bat.copy(up = false, down = true)))

        W_CODE    -> this.copy(p1 = p1.copy(bat = p1.bat.copy(up = true, down = false)))

        S_CODE    -> this.copy(p1 = p1.copy(bat = p1.bat.copy(up = false, down = true)))

        else -> this
    }
}

/**
 * Função que movimenta a raquete 1:
 *  - Caso o Y da bola estiver abaixo da raquete, ela desce
 *  - Caso o Y da bola estiver acima da raquete ela sobe
 *  - Caso contráiro ela fica parada
 */
fun Game.moveByAI():Game{
    val newY = ball.pos.y + ball.vel.dy

    return if(ball.vel.dx < 0){
        when {
            newY > p1.bat.centerY() && p1.bat.nextBottomY() <= HEIGHT -> copy(p1=p1.copy(bat=p1.bat.down()))
            newY < p1.bat.centerY() && p1.bat.nextY() >= 0            -> copy(p1=p1.copy(bat=p1.bat.up()))
            else -> this
        }
    }
    else this
}


/**
 * Função que movimenta a raquete 2 através da coordenada Y do mouse
 */
fun Game.moveByMouse(me:MouseEvent) :Game =
    if (me.y - RACKET_HEIGHT/2 >=0 && me.y + RACKET_HEIGHT/2 <=HEIGHT)
        copy(p2 = p2.copy(bat= p2.bat.copy(pos=Position(p2.bat.pos.x, me.y-RACKET_HEIGHT/2))))
    else
        this
