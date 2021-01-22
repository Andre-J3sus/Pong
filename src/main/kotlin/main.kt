import pt.isel.canvas.*


/**
 * A version of the game PONG with two playing modes:
 *  - AI vs Player which plays with the mouse;
 *  - Player1(plays with the keys W and S) vs Player2(plays with the Up and Down arrows);
 */
fun main(){
    onStart {
        loadSounds("goal", "touchBat", "touchWall")    //Carregar os sons do jogo
        val arena = Canvas(WIDTH, HEIGHT, FIELD_GREEN) //Arena com o comprimento, altura e cor da janela do Canvas
        var pong = startConditions()                   //Jogo em condições iniciais


        arena.onTimeProgress(10){ //A cada 10 milisegundos:
            pong = when {
                pong.states.aiXPl -> pong.moveByAI()           //Movimentação da raquete 1 por Inteligência Artificial
                pong.states.plXPl -> pong.moveRacket2Players() //Movimentação de duas raquetes pelas setas
                else -> pong                                   //Sem movimentação caso estejamos no menu
            }
                .moveBall().checkGoal()    //Movemos o jogo e verificamos se a bola saiu da janela(houve golo)
            arena.drawGame(pong)           //Desenhamos o jogo
        }


        if (pong.states.playing) arena.onTimeProgress(5000){ pong = pong.speed() }
        else arena.onMouseDown { me-> pong = pong.checkClick(me)} //Verificar se houve click no botão


        arena.onMouseMove { me-> //Quando o mouse se movimenta:
            pong =
                if(!pong.states.playing) pong.checkMouseOn(me)  //Animação de rato a passar pelos botões
                else if(pong.states.aiXPl) pong.moveByMouse(me) //Mover raquete com o mouse
                else pong
        }


        arena.onKeyPressed { ke-> //Quando uma tecla é pressionada:
            pong = pong.startPlaying()//Se o jogo está parado, retorna um jogo em movimento
            if (pong.states.plXPl) pong = pong.checkKey(ke) //Se estiverem dois jogadores, verifica qual das teclas foi pressionada
        }
    }

    onFinish {
        println("Done!")
    }
}
