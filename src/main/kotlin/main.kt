import pt.isel.canvas.*


/**
 * A version of the game PONG with two players:
 *  - Player 1 plays with the keys W and S;
 *  - Player 2 plays with the Up and Down arrows;
 */
fun main(){
    onStart {
        loadSounds("goal", "touchBat", "touchWall") //Carregar os sons do jogo
        val arena = Canvas(WIDTH, HEIGHT, GREY)     //Arena com o comprimento, altura e cor da janela do Canvas
        var pong = startConditions()                //Jogo em condições iniciais

        //A cada 10 milisegundos:
        arena.onTimeProgress(10){
            arena.erase() //Limpamos a janela

            //pong = pong.startPlaying() //Verifica se estamos em jogo e a bola está parada, se sim retorna um jogo com a bola a mover-se

            pong = pong.moveByAI()  //Movimentação da raquete 1 por Inteligência Artificial

            pong = pong.moveBall().checkGoal()    //Movemos o jogo e verificamos se a bola saiu da janela(houve golo)
            arena.drawGame(pong)                  //Desenhamos o jogo
        }

        arena.onMouseMove { me->
            pong = pong.moveByMouse(me) //Mover raquete com o mouse
            pong = pong.checkMouseOn(me)//Animação de rato a passar pelos botões
        }

        arena.onMouseDown { me->
            pong = pong.checkClick(me) //Verificar se houve click no botão
        }

        //Quando uma tecla é pressionada: retorna um jogo com alguma das raquetes movidas, ou não
        arena.onKeyPressed { ke->
            pong = pong.startPlaying()//Se o jogo está parado, retorna um jogo em movimento
            pong = pong.moveRacket(ke)                          //Movimenta as raquetes do jogo
        }
    }

    onFinish {
        println("Done!")
    }
}
