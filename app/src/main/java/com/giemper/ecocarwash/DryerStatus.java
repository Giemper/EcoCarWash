package com.giemper.ecocarwash;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by guillermo.magdaleno on 4/27/2018.
 */

public class DryerStatus
{
    private List<Dryer> available = new ArrayList<>();
    private List<Dryer> busy = new ArrayList<>();

    public void addDryer(Dryer dry)
    {
        available.add(dry);
    }

    public void removeDryer(Dryer dry)
    {
        available.remove(dry);
    }

    public void getBusy()
    {
        Dryer dry = available.get(0);
        available.remove(dry);
        busy.add(dry);
    }

    public void getAvailable(Dryer dry)
    {
        busy.add(dry);
        available.add(dry);
    }
}
