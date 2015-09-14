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

package org.artags.server.web;

import com.google.appengine.api.images.Image;
import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.images.Transform;
import org.artags.server.business.TagThumbnail;
import org.artags.server.business.TagThumbnailDAO;

/**
 *
 * @author pierre
 */
public class ToolsJspBean
{

    public String resizeThumbnail()
    {
        int size = 200;
        TagThumbnailDAO dao = new TagThumbnailDAO();

        for (TagThumbnail t : dao.findAll())
        {
            byte[] oldImageData = t.getImage();

            ImagesService imagesService = ImagesServiceFactory.getImagesService();

            Image oldImage = ImagesServiceFactory.makeImage(oldImageData);
            Transform resize = ImagesServiceFactory.makeResize( size, size );

            Image newImage = imagesService.applyTransform(resize, oldImage);

            byte[] newImageData = newImage.getImageData();
            t.setImage(newImageData);
            dao.update(t);

        }

        return "Resizing OK - new dimension is : " + size;


    }
}
