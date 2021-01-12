import pt.isel.canvas.*

data class Player(val bat: Racket, val score:Int = 0) //Classe Player com raquete e pontuação

const val LIMIT_SCORE = 1 //Costante de ponto necessários para ganhar um jogo

fun Player.addScore() = Player(bat, score+1) //Função que retorna um player com mais um ponto
fun Player.wins():Boolean = score == LIMIT_SCORE //True se o player atingiu os pontos necessários para ganhar

/**
 * Função que desenha no CANVAS o player que venceu, recebendo o número que o define
 */
fun Canvas.writeWins(pn:Int){
    drawText(2*WIDTH/7-4, SCORE_Y*2 + 4, "Player $pn Wins!", BLACK, 38)
    drawText(2*WIDTH/7, SCORE_Y*2, "Player $pn Wins!", RED, 38)
}
