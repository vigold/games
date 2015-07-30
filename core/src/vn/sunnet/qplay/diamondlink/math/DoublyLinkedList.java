/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.sunnet.qplay.diamondlink.math;

/**
 *
 * @author VuTungLinh
 */


public class DoublyLinkedList
{
    public DoublyLinkedList()
    {
        m_ffe = new Element();
        m_ffi = new Iterator( m_ffe );
        clear();
    }

    public void clear()
    {
        m_ffe.c_next = m_ffe.c_prev = m_ffe;
        m_ffe.c_data = null;
        m_nElement  = 0;
    }

    public void push_back( Object data )
    {
        Element newElement = new Element();

        newElement.c_data = data;
        newElement.c_prev = m_ffe.c_prev;
        newElement.c_next = m_ffe;
        m_ffe.c_prev.c_next = newElement;
        m_ffe.c_prev        = newElement;
        ++m_nElement;
    }

    public void push_front( Object data )
    {
        Element newElement = new Element();

        newElement.c_data   = data;
        newElement.c_prev   = m_ffe;
        newElement.c_next   = m_ffe.c_next;
        m_ffe.c_next.c_prev = newElement;
        m_ffe.c_next        = newElement;
        ++m_nElement;
    }
    
    public void replace( int index, Object newData )
    {
        if ( (index < 0) || (index >= m_nElement) )
            return;

        Iterator i = first();

        while ( index-- != 0 )
            i.fwrd();
        
        i.m_element.c_data = newData;
    }
    
    public Object pop_back()
    {
        if ( m_nElement == 0 )
            return null;

        Element removedElement = m_ffe.c_prev;

        removedElement.drop();
        --m_nElement;

        return removedElement.c_data;
    }

    public Object pop_front()
    {
        if ( m_nElement == 0 )
            return null;

        Element removedElement = m_ffe.c_next;

        removedElement.drop();
        --m_nElement;

        return removedElement.c_data;
    }

    public Object peek_back()
    {
        return m_ffe.c_prev.c_data;
    }

    public Object peek_front()
    {
        return m_ffe.c_next.c_data;
    }

    public Iterator first()
    {
        if ( m_nElement == 0 )
            return m_ffi;

        Iterator ret = m_ffi._clone();

        ret.fwrd();
        return ret;
    }

    public Iterator last()
    {
        if ( m_nElement == 0 )
            return m_ffi;

        Iterator ret = m_ffi._clone();

        ret.back();
        return ret;
    }

    public Iterator end()
    {
        return m_ffi;
    }

    public int length()
    {
        return m_nElement;
    }

    public Object get( int index )
    {
        if ( (index < 0) || (index >= m_nElement) )
            return null;

        Iterator i = first();

        while ( index-- != 0 )
            i.fwrd();

        return i.data();
    }
    
    public int getIndex( Object data )
    {
        Iterator i = first();
        int index = 0;
        
        while ( !i.equals( m_ffi ) )
        {
            if ( data == i.data() )
                return index;
            
            ++index;
            i.fwrd();
        }
        
        return -1;
    }

    public void erase( int index )
    {
        Iterator i = first();

        while ( index-- != 0 )
            i.fwrd();

        i.m_element.drop();
        --m_nElement;
    }

    public void erase( Object data )
    {
        Iterator i = first();

        for ( ; !i.equals( m_ffi ); i.fwrd() )
        {
            if ( i.data() == data )
            {
                i.m_element.drop();
                --m_nElement;
				return;
            }
        }
    }

    private final Iterator  m_ffi;
    private Element         m_ffe;
    private int             m_nElement;
}
