package snake;
import com.googlecode.lanterna.TerminalFacade;
import com.googlecode.lanterna.gui.GUIScreen;
import com.googlecode.lanterna.gui.dialog.DialogButtons;
import com.googlecode.lanterna.gui.dialog.DialogResult;
import com.googlecode.lanterna.gui.dialog.MessageBox;
import com.googlecode.lanterna.input.Key;
import com.googlecode.lanterna.input.Key.Kind;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.ScreenCharacterStyle;
import com.googlecode.lanterna.terminal.Terminal;
import java.util.Random;

public class Snake {
    public static void main(String[] args) throws InterruptedException {
        Screen s = TerminalFacade.createScreen();
        GUIScreen gs = new GUIScreen(s);
        s.startScreen();
        s.getTerminal().setCursorVisible(false);
        s.setCursorPosition(null);
        Random r = new Random();
        
        int gameSpeed = 250;
        
        while(true){
        int [][] snakeTail  = {};
        int snakex          = 10;
        int snakey          = 10;
        int snakedirx       = 1;
        int snakediry       = 0;
        int foodx           = 30;
        int foody           = 10;
        while(true){
            int foodxCandidate = r.nextInt(s.getTerminalSize().getColumns());
            int foodyCandidate = r.nextInt(s.getTerminalSize().getRows());
            if(foodxCandidate==foodx && foodyCandidate==foody){
            continue;
            }
            foodx = foodxCandidate;
            foody = foodyCandidate;
            break;
        }
        
        mainLoop:
        while(true){
            /* User input proccessing */
            Key pressedKey = s.readInput();
            if(pressedKey != null){
                Kind presedKeyKind = pressedKey.getKind();
                if(presedKeyKind==Kind.ArrowLeft||
                   presedKeyKind==Kind.ArrowRight||
                   presedKeyKind==Kind.ArrowUp||
                   presedKeyKind==Kind.ArrowDown){
                    snakedirx = 0;
                    snakediry = 0;
                }
                switch(presedKeyKind){
                    case ArrowLeft:
                        snakedirx = -1;
                        break;
                    case ArrowRight:
                        snakedirx = 1;
                        break;
                    case ArrowUp:
                        snakediry = -1;
                        break;
                    case ArrowDown:
                        snakediry = 1;
                        break;
                }
            }
            
            int oldSnakeX   = snakex;
            int oldSnakeY   = snakey;
            
            /* Snake movement processing */
            snakex += snakedirx;
            snakey += snakediry;
            
            int oldTailEndX = oldSnakeX;
            int oldTailEndY = oldSnakeY;
            
            if(snakeTail.length>0){
                oldTailEndX = snakeTail[snakeTail.length-1][0];
                oldTailEndY = snakeTail[snakeTail.length-1][1];
            }
            
            for(int i=snakeTail.length-1;i>0;i--){
                snakeTail[i][0] = snakeTail [i-1][0];
                snakeTail[i][1] = snakeTail [i-1][1];
            }
            
            if(snakeTail.length>0){
                snakeTail[0][0] = oldSnakeX;
                snakeTail[0][1] = oldSnakeY;
            }
            
            /* Process feed */
            if(snakex==foodx && snakey==foody){
                int[][] newTail = new int[snakeTail.length+1][];
                System.arraycopy(snakeTail, 0, newTail, 0, snakeTail.length);
                newTail[snakeTail.length] = new int[]{oldTailEndX, oldTailEndY};
                snakeTail=newTail;
                gameSpeed -= 5;
                while(true){
                    int foodxCandidate = r.nextInt(35);
                    int foodyCandidate = r.nextInt(35);
                    if(foodxCandidate==foodx && foodyCandidate==foody){
                        continue;
                    }
                    foodx = foodxCandidate;
                    foody = foodyCandidate;
                    break;
                }
            }
            
            /* Screen filling */
            s.clear();
            s.putString(snakex, snakey, "@", Terminal.Color.RED, Terminal.Color.BLACK, ScreenCharacterStyle.Bold);
            for (int[] snakeTail1 : snakeTail) {
                s.putString(snakeTail1[0], snakeTail1[1], "+", Terminal.Color.RED, Terminal.Color.BLACK, ScreenCharacterStyle.Bold);
            }
            s.putString(foodx, foody, "#", Terminal.Color.GREEN, Terminal.Color.BLACK, ScreenCharacterStyle.Bold);
            s.refresh();
            
            for(int i=0;i<snakeTail.length;i++){
                if(snakex == snakeTail[i][0] && snakey==snakeTail[i][1]){
                    MessageBox.showMessageBox(gs, "Game Over", "You ate yor own tail\nYour score: " + snakeTail.length);
                    s.clear();
                    s.refresh();
                    break mainLoop;
                }
            }
            
            if(snakex<0 || snakey<0 || snakex>s.getTerminalSize().getColumns()-1 || snakey>s.getTerminalSize().getRows()-1){
                MessageBox.showMessageBox(gs, "Game Over", "You reached the end of map\nYour score: " + snakeTail.length);
                s.clear();
                s.refresh();
                break mainLoop;
            }
            
            Thread.sleep(gameSpeed);
        }    
        DialogResult res = MessageBox.showMessageBox(gs, "Play again?", "Do you want to play again?", DialogButtons.YES_NO);
                if (res==DialogResult.NO){
                break;
                }
        }
        System.exit(0);
    }
}
