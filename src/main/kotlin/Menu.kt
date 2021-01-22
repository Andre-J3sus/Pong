import pt.isel.canvas.*


//Data class menu com botão de play
data class Menu (val onePlayerButton:Button, val twoPlayersButton:Button, val menuButton: Button)

//Data classe botão, com posição, texto e Boolean que fica true quando o mouse está em cima do mesmo
data class Button(val pos:Position, val txt:String, val mouseOn:Boolean)

//Constantes de:
const val BUTTON_WIDTH = 151   //Comprimento do botão
const val BUTTON_BORDER = 2 //Borda do botão
const val BUTTON_HEIGHT = 41  //Altura do botão
const val DARK_CYAN = 0x00CCCC //Cor Cyan mais escura
const val DARK_RED = 0x8B0000 //Encarnado
const val PONG_X=2*WIDTH/7  //Coordenada X do texto PONG
const val PONG_Y = SCORE_Y*2 //Coordenada Y do texto PONG
const val PONG_FONT_SIZE= 82 //Tamanho da fonte texto PONG
const val PONG_SHADOW_DISTANCE= 4 //Distância da sombra do texto PONG


//Funções que retornam:
fun Button.centeredX() = pos.x - BUTTON_WIDTH/2 //a coordenada X do centro do botão
fun Button.rangeX() = centeredX() .. centeredX()+BUTTON_WIDTH //o range de coordenadas X do  botão
fun Button.rangeY() = pos.y .. pos.y+BUTTON_HEIGHT //o range de coordenadas Y do botão
fun Button.mouseOnButton(me:MouseEvent) = me.x in rangeX() && me.y in rangeY() //true se o mouse estiver em cima do botão


/**
 * Função que desenha o botão
 */
fun Canvas.drawButton(b:Button, color:Int, darkColor:Int){
    if(b.mouseOn) drawRect(b.centeredX(), b.pos.y, BUTTON_WIDTH, BUTTON_HEIGHT, darkColor) //Se o rato estiver em cima
    else drawRect(b.centeredX(), b.pos.y, BUTTON_WIDTH, BUTTON_HEIGHT, color) //Se o rato não estiver em cima

    drawRect(b.centeredX()-BUTTON_BORDER/2, b.pos.y-BUTTON_BORDER/2, BUTTON_WIDTH+BUTTON_BORDER/2, BUTTON_HEIGHT+BUTTON_BORDER/2, BLACK, BUTTON_BORDER) //Borda do botão
    drawText(b.centeredX()+1, b.pos.y + 5*BUTTON_HEIGHT/6, b.txt, BLACK, 32) //Texto
}


/**
 * Função que desenha o menu com os botões e o título do jogo
 */
fun Canvas.drawMenu(m:Menu){
    drawText( PONG_X- PONG_SHADOW_DISTANCE, PONG_Y + PONG_SHADOW_DISTANCE, "PONG", BLACK, PONG_FONT_SIZE) //Sombra
    drawText(PONG_X, PONG_Y, "PONG", GOLD, PONG_FONT_SIZE) //Palavra
    drawButton(m.onePlayerButton, CYAN, DARK_CYAN) //Botão 1Player
    drawButton(m.twoPlayersButton, RED, DARK_RED) //Botão 2Players
}

/**
 * Função que retorna um jogo, que, caso o mouse esteja em cima do botão, tem o menu com o botão com essa indicação
 */
fun Game.checkMouseOn(me:MouseEvent) =
    when{
        states.menu && menu.onePlayerButton.mouseOnButton(me)->//Mouse no botão 1 PLAYER
            copy(menu = menu.copy(onePlayerButton = menu.onePlayerButton.copy(mouseOn = true)))

        states.menu && menu.twoPlayersButton.mouseOnButton(me)->//Mouse no botão 2 PLAYERS
            copy(menu = menu.copy(twoPlayersButton = menu.twoPlayersButton.copy(mouseOn = true)))

        states.finished && menu.menuButton.mouseOnButton(me) ->//Mouse no botão menu
            copy(menu = menu.copy(menuButton = menu.menuButton.copy(mouseOn = true)))

        else->//Mouse em nenhum botão
            copy(menu = menu.copy(
                onePlayerButton = menu.onePlayerButton.copy(mouseOn = false),
                twoPlayersButton = menu.twoPlayersButton.copy(mouseOn = false),
                menuButton = menu.menuButton.copy(mouseOn = false)
            ))
    }


/**
 * Função que verifica se existe click do mouse em cima de algum botão
 */
fun Game.checkClick(me: MouseEvent) =
    if (states.menu)
        when{
            menu.onePlayerButton.mouseOnButton(me) -> copy(states = states.copy(menu=false, aiXPl = true))
            menu.twoPlayersButton.mouseOnButton(me) -> copy(states = states.copy(menu=false, plXPl = true))
            else -> this
        }
    else if (!states.playing && menu.menuButton.mouseOnButton(me)) startConditions()
    else this
