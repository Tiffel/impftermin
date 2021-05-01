package tech.cscheer.impfen.selenium.page;

/**
 * Stand 2021-05-01
 * <p>
 * <select aria-required="true" data-select2-id="1" style="display: block;" tabindex="-1" class="select2-hidden-accessible"
 * aria-hidden="true">
 * <option value="1619875020237_NULL" data-select2-id="3"></option>
 * <option value="BG01">Stadthalle Belgern, Mühlberger Str. 37, 04874 Belgern-Schildau</option>
 * <option value="BN01">ehem. Aldi Markt Borna, Oststraße 3a, 04552 Borna</option>
 * <option value="BZ01">Sporthalle am Flughafen, Macherstraße 146, 01917 Kamenz</option>
 * <option value="CN01">Richard-Hartmann-Halle, Fabrikstraße 9, 09111 Chemnitz</option>
 * <option value="DD01">Messe Dresden, Messering 6, 01067 Dresden</option>
 * <option value="EC01">Spektrum Treuen/Eich (ehem. Baumarkt), Rebesgrüner Str. 9, 08223 Treuen OT Eich</option>
 * <option value="EZ01">Festhalle Annaberg-Buchholz, Ernst-Roch-Straße 4, 09456 Annaberg-Buchholz</option>
 * <option value="GR01">TIZ Grimma, Muldentalhalle, Südstraße 80, 04668 Grimma</option>
 * <option value="LB01">Messehalle Löbau, Görlitzer Str. 2, 02708 Löbau</option>
 * <option value="LZ01">Messe Leipzig, Messe-Allee 1, 04356 Leipzig</option>
 * <option value="MW01">Mittweida über Simmel-Markt (ehem. EKZ), Schillerstraße 1, 09648 Mittweida</option>
 * <option value="PL01">IZ Plauen, Europaratstraße 5, 08523 Plauen</option>
 * <option value="PN01">Aldi Pirna Jessen, Radeberger Str. 1h, 01796 Pirna</option>
 * <option value="RI01">Sachsen-Arena Riesa, Am Sportzentrum 5, 01589 Riesa</option>
 * <option value="ZW01">Stadthalle Zwickau, Bergmannsstraße 1, 08056 Zwickau</option>
 * </select>
 */
public enum Impzentrum {
    DRESDEN("DD01"),
    PIRNA("PN01");

    private String value;

    Impzentrum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
