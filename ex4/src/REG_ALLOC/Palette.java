package REG_ALLOC;

import java.util.HashMap;

public class Palette
{
    /*
     * palette is a mapping between virtual temporary registers (as defined in the
     * TEMP package), and real temporary registers ($t0 - $t9 in MIPS).
     */
    private HashMap<Integer, Integer> palette = new HashMap<>();
    private static Palette instance = null;

    protected Palette() {}

    public static Palette getInstance()
    {
        if (instance == null)
        {
            instance = new Palette();
        }

        return instance;
    }

    /**
     * Add a mapping between the temporary register number, and its "color"
     * (The register we will use for this TEMP).
     * @param temp_reg_idx Temporary register number (as defined by TEMP.getSerialNumber()).
     * @param color The color of this register - $t0, or $t1, ..., or $t9.
     */
    public void addColor(Integer temp_reg_idx, Integer color)
    {
        palette.put(temp_reg_idx, color);
    }

    /**
     * Get the matching color for this temp.
     * @param temp_reg_idx The temporary register.
     * @return the color of this register, or null if there isn't any mapping yet.
     */
    public Integer getColor(Integer temp_reg_idx)
    {
        return palette.get(temp_reg_idx);
    }
}
