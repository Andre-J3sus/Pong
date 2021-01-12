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

            if (pong.states.playing && !pong.ball.isMoving())
                pong = pong.startPlaying() //Se estamos em jogo e a bola está parada retorna um jogo com a bola a mover-se

            pong = pong.moveBall().checkGoal()    //Movemos o jogo e verificamos se a bola saiu da janela(houve golo)
            arena.drawGame(pong)                  //Desenhamos o jogo
        }

        arena.onMouseMove { me-> //Animação de rato a passar pelos botões
            pong = if (pong.states.menu && pong.menu.pb.mouseOnButtom(me))
                pong.copy(menu = pong.menu.copy(pb = pong.menu.pb.copy(mouse =  true)))
            else
                pong.copy(menu = pong.menu.copy(pb = pong.menu.pb.copy(mouse =  false)))
        }

        arena.onMouseDown { me-> //Clicar no botão
            if (pong.states.menu && pong.menu.pb.mouseOnButtom(me)) pong = pong.copy(states = pong.states.copy(menu=false))
        }

        //Quando uma tecla é pressionada: retorna um jogo com alguma das raquetes movidas, ou não
        arena.onKeyPressed { ke->
            if (!pong.states.playing && !pong.states.menu) pong = pong.startPlaying()//Se o jogo está parado, retorna um jogo em movimento
            pong = pong.moveRacket(ke)                          //Movimenta as raquetes do jogo
        }
    }

    onFinish {
        println("Done!")
    }
}
