import pt.isel.canvas.*

data class Menu (val pb:Button)
data class Button(val pos:Position, val txt:String, val mouse:Boolean)

const val BUTTON_WIDTH = 81
const val BUTTON_HEIGHT = 41
const val DARK_CYAN = 0x00CCCC

fun Button.centeredX() = pos.x - BUTTON_WIDTH/2
fun Button.rangeX() = centeredX() .. centeredX()+BUTTON_WIDTH
fun Button.rangeY() = pos.y .. pos.y+BUTTON_HEIGHT
fun Button.mouseOnButtom(me:MouseEvent) = me.x in rangeX() && me.y in rangeY()


fun Canvas.drawButton(b:Button){
    if(b.mouse) drawRect(b.centeredX(), b.pos.y, BUTTON_WIDTH+1, BUTTON_HEIGHT+1, DARK_CYAN)
    else drawRect(b.centeredX(), b.pos.y, BUTTON_WIDTH+1, BUTTON_HEIGHT+1, CYAN)

    drawRect(b.centeredX()-2, b.pos.y-2, BUTTON_WIDTH+4, BUTTON_HEIGHT+4, BLACK, 4)
    drawText(b.centeredX()+1, b.pos.y + 5*BUTTON_HEIGHT/6, b.txt, BLACK, 32)
}


fun Canvas.drawMenu(m:Menu){
    drawText(2*WIDTH/7-4, SCORE_Y*2 + 4, "MENU", BLACK, 82)
    drawText(2*WIDTH/7, SCORE_Y*2, "MENU", BLUE, 82)
    drawButton(m.pb)
}
