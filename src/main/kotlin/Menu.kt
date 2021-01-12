import pt.isel.canvas.*

data class Menu (val pb:Buttom)
data class Buttom(val pos:Position, val txt:String, val mouse:Boolean)

const val BUTTOM_WIDTH = 81
const val BUTTOM_HEIGHT = 41
const val DARK_CYAN = 0x00CCCC

fun Buttom.centeredX() = pos.x - BUTTOM_WIDTH/2
fun Buttom.rangeX() = centeredX() .. centeredX()+BUTTOM_WIDTH
fun Buttom.rangeY() = pos.y .. pos.y+BUTTOM_HEIGHT
fun Buttom.mouseOnButtom(me:MouseEvent) = me.x in rangeX() && me.y in rangeY()


fun Canvas.drawButtom(b:Buttom){
    if(b.mouse) drawRect(b.centeredX(), b.pos.y, BUTTOM_WIDTH+1, BUTTOM_HEIGHT+1, DARK_CYAN)
    else drawRect(b.centeredX(), b.pos.y, BUTTOM_WIDTH+1, BUTTOM_HEIGHT+1, CYAN)

    drawRect(b.centeredX()-2, b.pos.y-2, BUTTOM_WIDTH+4, BUTTOM_HEIGHT+4, BLACK, 4)
    drawText(b.centeredX()+1, b.pos.y + 5*BUTTOM_HEIGHT/6, b.txt, BLACK, 32)
}


fun Canvas.drawMenu(m:Menu){
    drawText(2*WIDTH/7-4, SCORE_Y*2 + 4, "MENU", BLACK, 82)
    drawText(2*WIDTH/7, SCORE_Y*2, "MENU", BLUE, 82)
    drawButtom(m.pb)
}
