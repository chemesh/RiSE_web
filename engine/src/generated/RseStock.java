//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2021.04.05 at 09:11:33 PM IDT 
//


package generated;

import javax.xml.bind.annotation.*;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;all>
 *         &lt;element ref="{}rse-symbol"/>
 *         &lt;element ref="{}rse-company-name"/>
 *         &lt;element ref="{}rse-price"/>
 *       &lt;/all>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {

})
@XmlRootElement(name = "rse-stock")
public class RseStock {

    @XmlElement(name = "rse-symbol", required = true)
    protected String rseSymbol;
    @XmlElement(name = "rse-company-name", required = true)
    protected String rseCompanyName;
    @XmlElement(name = "rse-price")
    protected int rsePrice;

    /**
     * Gets the value of the rseSymbol property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRseSymbol() {
        return rseSymbol;
    }

    /**
     * Sets the value of the rseSymbol property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRseSymbol(String value) {
        this.rseSymbol = value;
    }

    /**
     * Gets the value of the rseCompanyName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRseCompanyName() {
        return rseCompanyName;
    }

    /**
     * Sets the value of the rseCompanyName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRseCompanyName(String value) {
        this.rseCompanyName = value;
    }

    /**
     * Gets the value of the rsePrice property.
     * 
     */
    public int getRsePrice() {
        return rsePrice;
    }

    /**
     * Sets the value of the rsePrice property.
     * 
     */
    public void setRsePrice(int value) {
        this.rsePrice = value;
    }

}
