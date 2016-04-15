package gr.eurobank.utils.converters;

public class Main {

    /**
     * Return the ISO 843:1997 transcription of the input Greek text.
     * Any non-Greek characters will be ignored and printed as they were.
     */
    static String Romanize(String greekText) {
        final String simple_translation_greek = "άβδέζήιίϊΐκλνξόπρσςτυύϋΰφωώ";
        final String simple_translation_latin = "avdeziiiiiklnxoprsstyyyyfoo";

        final String digraph_translation_greek = "θχψ";
        final String digraph_translation_latin = "thchps";

        final String digraph_ypsilon_greek = "αεη";
        final String digraph_ypsilon_latin = "aei";
        final String digraph_ypsilon_beta = "βγδζλμνραάεέηήιίϊΐοόυύϋΰωώ";
        final String digraph_ypsilon_phi = "θκξπστφχψ";

        StringBuilder result = new StringBuilder();
        int cursor = 0;
        while (cursor < greekText.length()) {
            char letter = greekText.charAt(cursor);
            char prev_letter = (cursor > 0) ? greekText.charAt(cursor - 1) : '\0';
            char next_letter = (cursor < greekText.length() - 1) ? greekText.charAt(cursor + 1) : '\0';
            char third_letter = (cursor < greekText.length() - 2) ? greekText.charAt(cursor + 2) : '\0';
            String newLetter = "";
            boolean is_upper = (Character.toUpperCase(letter) == letter);
            boolean is_upper2 = (Character.toUpperCase(next_letter) == next_letter);
            letter = Character.toLowerCase(letter);
            prev_letter = Character.toLowerCase(prev_letter);
            next_letter = Character.toLowerCase(next_letter);
            third_letter = Character.toLowerCase(third_letter);

            if (simple_translation_greek.indexOf(letter) > 0) {
                newLetter = "" + simple_translation_latin.charAt(simple_translation_greek.indexOf(letter));
            } else if (digraph_translation_greek.indexOf(letter) > 0) {
                int diphthong_index = digraph_translation_greek.indexOf(letter);
                newLetter = digraph_translation_latin.substring(diphthong_index * 2, diphthong_index * 2 + 2);
            } else if (digraph_ypsilon_greek.indexOf(letter) > 0) {
                newLetter = "" + digraph_ypsilon_latin.charAt(digraph_ypsilon_greek.indexOf(letter));
                if ("υύ".indexOf(next_letter) > 0) {
                    if (digraph_ypsilon_beta.indexOf(third_letter) > 0) {
                        newLetter += "v";
                        cursor++;
                    } else if (digraph_ypsilon_phi.indexOf(third_letter) > 0) {
                        newLetter += "f";
                        cursor++;
                    }
                }
            } else if (letter == 'γ') {
                if (next_letter == 'γ') {
                    newLetter = "ng";
                    cursor++;
                } else if (next_letter == 'ξ') {
                    newLetter = "nx";
                    cursor++;
                } else if (next_letter == 'χ') {
                    newLetter = "nch";
                    cursor++;
                } else {
                    newLetter = "g";
                }
            } else if (letter == 'μ') {
                if (next_letter == 'π') {
                    if (Character.isWhitespace(prev_letter) || Character.isWhitespace(third_letter)) {
                        newLetter = "b";
                        cursor++;
                    } else {
                        newLetter = "mp";
                        cursor++;
                    }
                } else {
                    newLetter = "m";
                }
            } else if (letter == 'ο') {
                newLetter = "o";
                if ("υύ".indexOf(next_letter) > 0) {
                    newLetter += "u";
                    cursor += 1;
                }
            } else {
                newLetter = String.valueOf(letter);
            }
            if (is_upper) {
                newLetter = newLetter.substring(0, 1).toUpperCase() +
                        (is_upper2 ? newLetter.substring(1).toUpperCase() : newLetter.substring(1).toLowerCase());
            }
            result.append(newLetter);
            cursor++;
        }
        return result.toString();
    }

    public static void main(String[] args) {
        System.out.printf("Γιώργος Σχίζας\n");
        System.out.printf(Romanize("Γιώργος Σχίζας\n"));
        assert Romanize("Γιώργος Σχίζας").equals("Giorgos Schizas");
        assert Romanize("Θανάσης ΘΑΝΑΣΗΣ θΑνάσης ΘΑνάσης").equals("Thanasis THANASIS thAnasis THAnasis");
        assert Romanize("Αντώνης Ψαράς με ψάρια").equals("Antonis Psaras me psaria");
        assert Romanize("Αυγά αύριο παύση").equals("Avga avrio pafsi");
        assert Romanize("Άγγελος αρχάγγελος").equals("Angelos archangelos");
        assert Romanize("Ξάδελφος εξ αγχιστείας").equals("Xadelfos ex anchisteias");
        assert Romanize("Ακούμπα κάτω τα μπαούλα Γιακούμπ").equals("Akoumpa kato ta baoula Giakoub");
        assert Romanize("Ζεύξη Ρίου-Αντιρρίου").equals("Zefxi Riou-Antirriou");
        assert Romanize("μεταγραφή").equals("metagrafi");
        assert Romanize("Ούτε το αγγούρι ούτε η αγκινάρα γράφονται με γξ").equals("Oute to angouri oute i agkinara grafontai me nx");
        assert Romanize("ΟΥΡΑΝΟΣ Ουρανός ουρανός οϋρανός").equals("OURANOS Ouranos ouranos oyranos");
        assert Romanize("Έχω ελέγξει το 100% της μεθόδου").equals("Echo elenxei to 100% tis methodou");
        assert Romanize("παπά").equals("papa");
        assert Romanize("παπα").equals("papa");
        assert Romanize("πάπά").equals("papa");
        assert Romanize("α").equals("a");
    }
}
