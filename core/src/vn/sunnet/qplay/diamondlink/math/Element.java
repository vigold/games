package vn.sunnet.qplay.diamondlink.math;

public class Element
{
    public Element() {}

    public void drop()
    {
        /* $if SPUKMK2ME_DEBUG$
        if ( (c_prev == null) || (c_next == null) )
        {
            Logger.Trace( "Dropping unassigned element." );
        }
        $endif$ */

        c_prev.c_next = c_next;
        c_next.c_prev = c_prev;
        c_next = c_prev = null;
    }

    public Object   c_data;
    public Element  c_next, c_prev;
}
