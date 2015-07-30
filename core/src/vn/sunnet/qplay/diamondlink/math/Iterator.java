package vn.sunnet.qplay.diamondlink.math;


public class Iterator
{
    public Iterator( Element element )
    {
        m_element = element;
    }

    public void fwrd()
    {
        m_element = m_element.c_next;
    }

    public void back()
    {
        m_element = m_element.c_prev;
    }
    
    public Element fwrdElement() {
		return m_element.c_next;
	}
    
    public Element backElement() {
    	return m_element.c_prev;
    }

    public Object data()
    {
        return m_element.c_data;
    }

    public Iterator _clone()
    {
        return new Iterator( m_element );
    }

    public boolean equals( Iterator iterator )
    {
        return m_element.c_data == iterator.data();
    }
    
    Element m_element;
}
