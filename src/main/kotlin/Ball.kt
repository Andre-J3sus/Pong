import pt.isel.canvas.*
import kotlin.math.sqrt
import kotlin.math.pow


data class Position(val x:Int, val y:Int) //Posição contém uma coordenada X e uma coordenada Y
data class Velocity(val dx:Int, val dy:Int) //Velocidade tem deslocamentos Dx e Dy
data class Ball(val pos:Position, val vel:Velocity) //Bola com posição e velocidade

typealias Collision = Pair<Int, Boolean> //Colisão é um par com Int(xLimite) e Boolean(true se colidiu)

const val BALL_RADIUS = 7//Raio da Bola

val dxRange = (-4..4).toList() - 0 //Lista com os possíveis deslocamentos Dx da bola
val dyRange = (-4..4).toList() - 0 //Lista com os possíveis deslocamentos Dy da bola


fun Ball.leaveByRight() = pos.x + vel.dy - BALL_RADIUS > WIDTH //True quando a bola sai pelo lado direito
fun Ball.leaveByLeft() = pos.x + vel.dy + BALL_RADIUS < 0      //True quando a bola sai pelo lado esquerdo
fun Ball.collideTopOrBottom() = pos.y + vel.dy + BALL_RADIUS >= HEIGHT || pos.y + vel.dy - BALL_RADIUS <= 0 //True se colidiu com as paredes
fun Ball.isMoving() = !(vel.dx == 0 && vel.dy == 0) //True se a bola está a mover-se e false se está parada


/**
 * Função que recebe uma bola e desenha a mesma
 */
fun Canvas.drawBall(b:Ball){
    drawCircle(b.pos.x, b.pos.y, BALL_RADIUS, CYAN)
}


/**
 * Função que retorna uma bola com a modificação e verifica se:
 *  - irá ultrapassar algum limite lateral, volta ao meio e fica parada
 *  - irá utrapassar o limite superior ou inferior, rebate
 *  - não bate em nenhuma parede, verifica se bate numa raquete
 */
fun Ball.move(gm:Game):Ball{
    val newX = pos.x + vel.dx
    val newY = pos.y + vel.dy

    return when{
        leaveByLeft() || leaveByRight()  -> {
            playSound("goal")
            Ball(Position(WIDTH/2, HEIGHT/2), Velocity(0, 0))
        }
        collideTopOrBottom()             -> {
            playSound("touchWall")
            Ball(Position(newX, pos.y), Velocity(vel.dx, -vel.dy))
        }
        else                             -> collideRacket(gm, newX, newY)
    }
}


/**
 * Função que calcula o valor mais próximo de outro num intervalo(range)
 */
fun clamp(value: Int, range: IntRange):Int =
    when{
        value < range.first -> range.first
        value > range.last  -> range.last
        else                -> value
    }


/**
 * Função que calcula a distância entre dois pontos através do teorema de pitágoras
 */
fun calculateDistance(cx: Int, cy: Int, closestX:Int, closestY: Int):Double =
    sqrt((cx.toDouble()-closestX.toDouble()).pow(2) + (cy.toDouble()-closestY.toDouble()).pow(2))


/**
 * Função que verifica se a bola colide com alguma das raquetes, se sim, retorna uma bola que rebate
 */
fun Ball.collideRacket(gm:Game, newX:Int, newY:Int):Ball{
    val collision = when{
        vel.dx > 0 && pos.x < gm.p2.bat.pos.x -> checkCollide(gm.p2.bat, gm.p2.bat.pos.x, newX, newY)
        vel.dx < 0 && pos.x > gm.p1.bat.pos.x -> checkCollide(gm.p1.bat, gm.p1.bat.pos.x + RACKET_WIDTH,newX, newY)
        else                                  -> Collision(newX, false)
    }

    return if (collision.second) {
        playSound("touchBat")
        Ball(Position(collision.first, newY), Velocity(-vel.dx, vel.dy))
    }
    else Ball(Position(newX, newY), vel)
}


/**
 * Função que retorna uma colisão em que o primeiro elemento é o X limite da bola e o segundo é Boolean: true se colide, false senão
 */
fun checkCollide(bat:Racket, closestX: Int, newX: Int, newY: Int):Collision{
    val closestY = clamp(newY, bat.rangeY())
    val xLimit = bat.xLimit()
    val collided = calculateDistance(newX, newY, closestX, closestY) <= BALL_RADIUS
    return Collision(xLimit, collided)
}
