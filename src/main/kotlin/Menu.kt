import pt.isel.canvas.*


//Data class menu com botão de play
data class Menu (val pb:Button)
//Data classe botão, com posição, texto e Boolean que fica true quando o mouse está em cima do mesmo
data class Button(val pos:Position, val txt:String, val mouseOn:Boolean)

//Constantes de:
const val BUTTON_WIDTH = 81   //Comprimento do botão
const val BUTTON_HEIGHT = 41  //Altura do botão
const val DARK_CYAN = 0x00CCCC //Cor Cyan mais escura

//Funções que retornam:
fun Button.centeredX() = pos.x - BUTTON_WIDTH/2 //a coordenada X do centro do botão
fun Button.rangeX() = centeredX() .. centeredX()+BUTTON_WIDTH //o range de coordenadas X do  botão
fun Button.rangeY() = pos.y .. pos.y+BUTTON_HEIGHT //o range de coordenadas Y do botão
fun Button.mouseOnButton(me:MouseEvent) = me.x in rangeX() && me.y in rangeY() //true se o mouse estiver em cima do botão

/**
 * Função que desenha o botão
 */
fun Canvas.drawButton(b:Button){
    if(b.mouseOn) drawRect(b.centeredX(), b.pos.y, BUTTON_WIDTH+1, BUTTON_HEIGHT+1, DARK_CYAN) //Se o rato estiver em cima
    else drawRect(b.centeredX(), b.pos.y, BUTTON_WIDTH+1, BUTTON_HEIGHT+1, CYAN) //Se o rato não estiver em cima

    drawRect(b.centeredX()-2, b.pos.y-2, BUTTON_WIDTH+4, BUTTON_HEIGHT+4, BLACK, 4) //Borda do botão
    drawText(b.centeredX()+1, b.pos.y + 5*BUTTON_HEIGHT/6, b.txt, BLACK, 32) //Texto
}


/**
 * Função que desenha o menu
 */
fun Canvas.drawMenu(m:Menu){
    drawText(2*WIDTH/7-4, SCORE_Y*2 + 4, "MENU", BLACK, 82) //Sombra
    drawText(2*WIDTH/7, SCORE_Y*2, "MENU", BLUE, 82) //Palavra
    drawButton(m.pb) //Botão
}

/**
 * Função que retorna um jogo, que, caso o mouse esteja em cima do botão, tem o menu com o botão com essa indicação
 */
fun Game.checkMouseOn(me:MouseEvent) =
    if (states.menu && menu.pb.mouseOnButton(me))
        copy(menu = menu.copy(pb = menu.pb.copy(mouseOn = true)))
    else
        copy(menu = menu.copy(pb = menu.pb.copy(mouseOn = false)))


/**
 * Função que verifica se existe click do mouse em cima do botão
 */
fun Game.checkClick(me: MouseEvent)=
    if (states.menu && menu.pb.mouseOnButton(me)) copy(states = states.copy(menu=false)) else this