import pt.isel.canvas.*

data class Player(val bat: Racket, val score:Int = 0) //Classe Player com raquete e pontuação

const val LIMIT_SCORE = 5 //Costante de ponto necessários para ganhar um jogo

fun Player.addScore() = Player(bat, score+1) //Função que retorna um player com mais um ponto
fun Player.wins():Boolean = score == LIMIT_SCORE //True se o player atingiu os pontos necessários para ganhar

/**
 * Função que desenha no CANVAS o player que venceu, recebendo o número que o define
 */
fun Canvas.writeWins(pn:Int){
    drawText(SCORE_X, HEIGHT/4, "Player $pn Wins!", RED)
}
