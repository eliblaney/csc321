import java.awt.Font;

/**
 * Driver class for the keyboard-based virtual dulcimer.
 *
 * @author Eli Blaney
 * @version 1.0
 */
public class DulcimerDriver {
    public static void main(String[] args) {
        String treble1Keys = "1   2   3   4   5   6   7   8   9   0   -   =";
        String treble1Notes = "C+ C#+ D+  D#+ E+  F+  F#+ G+  G#+ A+  A#+  B+";
        String treble2Keys = "q   w   e   r   t   y   u   i   o   p   [   ]";
        String treble2Notes = "C  C#  D   D#  E   F   F#  G   G#  A   A#  B";
        String bassKeys = "a   s   d   f   g   h   j   k   l   ;   '  \n";
        String dashes = "--- --- --- --- --- --- --- --- --- --- --- ---";
        String bassNotes = "C- C#- D-  D#- E-  F-  F#- G-  G#- A-  A#- B-";

        StdDraw.setFont(new Font("Monospaced", Font.PLAIN, 12));
        StdDraw.textLeft(0.00, 1.00, "DULCIMER KEY MAPPINGS");
        StdDraw.textLeft(0.00, 0.90, "        keys:  " + treble1Keys);
        StdDraw.textLeft(0.00, 0.87, "TREBLE 1      " + dashes);
        StdDraw.textLeft(0.00, 0.84, "       notes:  " + treble1Notes);
        StdDraw.textLeft(0.00, 0.70, "        keys:  " + treble2Keys);
        StdDraw.textLeft(0.00, 0.67, "TREBLE 2      " + dashes);
        StdDraw.textLeft(0.00, 0.64, "       notes:  " + treble2Notes);
        StdDraw.textLeft(0.00, 0.50, "        keys:  " + bassKeys.replace("\n", "RET"));
        StdDraw.textLeft(0.00, 0.47, "BASS          " + dashes);
        StdDraw.textLeft(0.00, 0.44, "       notes:  " + bassNotes);

        String keys = (treble1Keys + " " + treble2Keys + " " + bassKeys).replace(" ", "");

        Dulcimer dulc = new Dulcimer(treble1Notes + " " + treble2Notes + " " + bassNotes);
        while(true) {
            if(StdDraw.hasNextKeyTyped()) {
                int typed = keys.indexOf(StdDraw.nextKeyTyped());
                dulc.hammer(typed);
            }
            dulc.play();
        }
    }
}
