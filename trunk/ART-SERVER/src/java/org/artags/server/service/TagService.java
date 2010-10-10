/* Copyright (c) 2010 ARTags project owners (see http://artags.org)
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
package org.artags.server.service;

import org.artags.server.business.Tag;
import org.artags.server.business.TagDAO;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 *
 * @author pierre@artags.org
 */
public class TagService
{

    private static final String CACHE_KEY_ALL_TAGS = "alltags";

    public static List<Tag> getNearestTags(double latitude, double longitude, int max)
    {
        List<Tag> list = filter( getAllTags(), latitude, longitude);

        if( list.size() > max )
        {
            list = getNearestTags(list, latitude, longitude, max);
        }

        return list;
    }

    private static List<Tag> filter(List<Tag> list, double latitude, double longitude)
    {
        List<Tag> listFiltered = new ArrayList<Tag>();
        long lat = Tag.get10e6(latitude);
        long lon = Tag.get10e6(longitude);
        long latmin = lat - 100000;
        long latmax = lat + 100000;
        long lonmin = lon - 100000;
        long lonmax = lon + 100000;
        for (Tag t : list)
        {
            if ((t.getLat10e6() < latmin) || (t.getLat10e6() > latmax)
                    || (t.getLon10e6() < lonmin) || (t.getLon10e6() > lonmax))
            {
                continue;
            }
            listFiltered.add(t);
        }
        return listFiltered;
    }

    static List<Tag> getNearestTags(List<Tag> list, double latitude, double longitude, int max)
    {
        return getNearestTags(list, latitude, longitude, max, 10000L);
    }

    static List<Tag> getNearestTags(List<Tag> list, double latitude, double longitude, int max, long radius)
    {
        long r = radius * radius / 10000;
        List<SortTag> listSort = new ArrayList<SortTag>();
        for (Tag tag : list)
        {
            SortTag t = new SortTag(tag, latitude, longitude);
            if (t.dist < r)
            {
                listSort.add(t);
            }
        }

        if( listSort.size() > max )
        {
            Collections.sort(listSort);
        }

        List<Tag> listNearest = new ArrayList<Tag>();

        int count = (listSort.size() > max) ? max : listSort.size();
        for (int i = 0; i < count; i++)
        {
            listNearest.add(listSort.get(i).tag);
        }
        return listNearest;

    }

    public static List<Tag> getTagsByCriteria()
    {
        TagDAO dao = new TagDAO();
        List<Tag> list = dao.findAll();
        Collections.sort(list, new DateComparator());
        return list;
    }

    static class DateComparator implements Comparator
    {

        public int compare(Object t1, Object t2)
        {
            long date1 = ((Tag) t1).getDate();
            long date2 = ((Tag) t2).getDate();

            return (date2 < date1) ? -1 : 1;
        }
    }

    public static List<Tag> getAllTags()
    {
        List<Tag> allTags = (List<Tag>) CacheService.instance().get(CACHE_KEY_ALL_TAGS);
        if (allTags == null)
        {
            TagDAO dao = new TagDAO();
            allTags = dao.findAll();
            CacheService.instance().put(CACHE_KEY_ALL_TAGS , allTags );

        }
        return allTags;

    }
}
