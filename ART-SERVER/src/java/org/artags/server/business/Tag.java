/* Copyright (c) 2010-2015 ARTags project owners (see http://www.artags.org)
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.artags.server.business;

import com.google.appengine.api.datastore.Key;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import javax.persistence.Id;

/**
 *
 * @author Pierre Levy
 */
@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Tag implements Serializable
{

    private static final double X10E6 = 1000000.0;
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    @Id
    private Key idKey;
    @Persistent
    private String name;
    @Persistent
    private double lat;
    @Persistent
    private double lon;
    @Persistent
    private Key keyImage;
    @Persistent
    private Key keyThumbnail;
    @Persistent
    private Long lat10e6;
    @Persistent
    private Long lon10e6;
    @Persistent
    private Long date;
    @Persistent
    private Integer ratingSum;
    @Persistent
    private Integer ratingCount;
    @Persistent
    private Integer inappropriate;
    @Persistent 
    private Long dateUpdate;



    public Tag()
    {
    }

    public Tag(String name, double lat, double lon)
    {
        this.name = name;
        this.lat = lat;
        this.lon = lon;
        this.lat10e6 = (long) (lat * X10E6);
        this.lon10e6 = (long) (lon * X10E6);
        this.date = new Date().getTime();
        this.dateUpdate = new Date().getTime();
    }

    public Tag( Tag2 tag )
    {
        name = tag.getName();
        lat = tag.getLat();
        lat10e6 = tag.getLat10e6();
        lon = tag.getLon();
        lon10e6 = tag.getLon10e6();
        date = new Date().getTime();
        keyImage = tag.getKeyImage();
        keyThumbnail = tag.getKeyThumbnail();
        dateUpdate = tag.getDateUpdate();
    }

    public Long getId()
    {
        return idKey.getId();
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Key getKey()
    {
        return idKey;
    }

    public void setKey(Key key)
    {
        this.idKey = key;
    }

    public double getLat()
    {
        return this.lat;
    }

    public void setLat(double lat)
    {
        this.lat = lat;
        this.lat10e6 = (long) (lat * X10E6);
    }

    public double getLon()
    {
        return this.lon;
    }

    public void setLon(double lon)
    {
        this.lon = lon;
        this.lon10e6 = (long) (lon * X10E6);
    }

    /**
     * @return the keyImage
     */
    public Key getKeyImage()
    {
        return keyImage;
    }

    /**
     * @param keyImage the keyImage to set
     */
    public void setKeyImage(Key keyImage)
    {
        this.keyImage = keyImage;
    }

    /**
     * @return the keyThumbnail
     */
    public Key getKeyThumbnail()
    {
        return keyThumbnail;
    }

    /**
     * @param keyThumbnail the keyThumbnail to set
     */
    public void setKeyThumbnail(Key keyThumbnail)
    {
        this.keyThumbnail = keyThumbnail;
    }

    /**
     * @return the lat10e6
     */
    public long getLat10e6()
    {
        return lat10e6;
    }

    /**
     * @param lat10e6 the lat10e6 to set
     */
    public void setLat10e6(long lat10e6)
    {
        this.lat10e6 = lat10e6;
    }

    /**
     * @return the lon10e6
     */
    public long getLon10e6()
    {
        return lon10e6;
    }

    /**
     * @param lon10e6 the lon10e6 to set
     */
    public void setLon10e6(long lon10e6)
    {
        this.lon10e6 = lon10e6;
    }

    /**
     * @return the date
     */
    public long getDate()
    {
        return date;
    }

    /**
     * @param date the date to set
     */
   public void setDate(long date)
    {
        this.date = date;
    }

    public String getFormatedDate(Locale locale) throws UnsupportedEncodingException
    {
        DateFormat format = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
        String d = format.format(new Date(date)); 
        return new String( d.getBytes( "UTF-8" ) );
    }

    /**
     * @return the date
     */
    public Long getDateUpdate()
    {
        return dateUpdate;
    }

    /**
     * @param date the date to set
     */
    public void setDateUpdate(long date)
    {
        this.dateUpdate = date;
    }

    /**
     * @return the ratingSum
     */
    public int getRatingSum()
    {
        return ( ratingSum == null ) ? 0 : ratingSum.intValue();
    }

    /**
     * @param ratingSum the ratingSum to set
     */
    public void setRatingSum(int ratingSum)
    {
        this.ratingSum = ratingSum;
    }

    /**
     * @return the ratingCount
     */
    public int getRatingCount()
    {
        return ( ratingCount == null ) ? 0 : ratingCount.intValue();
    }

    /**
     * @param ratingCount the ratingCount to set
     */
    public void setRatingCount(int ratingCount)
    {
        this.ratingCount = ratingCount;
    }

    /**
     * @return the inappropriate
     */
    public int getInappropriate()
    {
        return inappropriate;
    }

    /**
     * @param inappropriate the inappropriate to set
     */
    public void setInappropriate(int inappropriate)
    {
        this.inappropriate = inappropriate;
    }

    public String getRating()
    {
        if( ratingCount == null )
        {
            return "No rating yet";
        }
        double rating = (double) ratingSum / (double) ratingCount;
        return String.format("%.2f/5", rating );
    }

    public long getDistance( long lat10e6 , long lon10e6 )
    {
        return (long) ( 0.08 * Math.sqrt( (double )( (this.lat10e6 - lat10e6)*(this.lat10e6 - lat10e6) + (this.lon10e6 - lon10e6)* (this.lon10e6 - lon10e6))));
    }

    public static long get10e6( double coord )
    {
        return (long) ( coord * X10E6 );
    }

}
