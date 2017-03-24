package com.cedricmartens.imagebot;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Gdx2DPixmap;
import com.badlogic.gdx.utils.Disposable;

import java.util.Random;

/**
 * Created by martens on 23/03/17.
 */
public class Image implements Disposable
{
    private final float MUTATION = 0.05f;

    private Gdx2DPixmap pixmap;
    private Texture texture;

    public Image(Texture texture)
    {
        this.texture = texture;
    }

    public Image(Image image)
    {
        this.pixmap = image.getPixmap();

        Random rand = new Random();
        for(int i = 0; i < pixmap.getWidth(); i++)
        {
            for(int j = 0; j < pixmap.getHeight(); j++)
            {
                float r = rand.nextFloat() * MUTATION;
                if(rand.nextBoolean())
                    r = -r;

                float g = rand.nextFloat() * MUTATION;
                if(rand.nextBoolean())
                    g = -g;

                float b = rand.nextFloat() * MUTATION;
                if(rand.nextBoolean())
                    b = -b;

                Color c = new Color(pixmap.getPixel(i, j));

                r += c.r;
                g += c.g;
                b += c.b;


                pixmap.setPixel(i, j, Color.rgba8888(r, g, b, 1f));
            }
        }
    }

    public Image(int w, int h)
    {
        Gdx2DPixmap pix = new Gdx2DPixmap(w, h, Gdx2DPixmap.GDX2D_FORMAT_RGBA8888);
        Random rand = new Random();
        for(int i = 0; i < w; i++)
        {
            for(int j = 0; j < h; j++)
            {
                pix.setPixel(i, j, Color.rgba8888(rand.nextFloat(), rand.nextFloat(),
                                                                rand.nextFloat(), 1f));
            }
        }
        pixmap = pix;
    }

    public float getFitness(Image image)
    {
        Gdx2DPixmap pix = image.getPixmap();
        float d = 0;
        for(int i = 0; i < pix.getWidth(); i++)
        {
            for(int j = 0; j < pix.getHeight(); j++)
            {
                Color c1 = new Color(pix.getPixel(i, j));
                Color c2 = new Color(this.pixmap.getPixel(i, j));

                d += Math.abs(c1.r - c2.r);
                d += Math.abs(c1.g - c2.g);
                d += Math.abs(c1.b - c2.b);
            }
        }
        return d;
    }

    public Gdx2DPixmap getPixmap() {
        if(pixmap != null)
            return pixmap;

        this.pixmap = new Gdx2DPixmap(texture.getWidth(), texture.getHeight(),
                                            Gdx2DPixmap.GDX2D_FORMAT_RGBA8888);

        texture.getTextureData().prepare();
        Pixmap pix = texture.getTextureData().consumePixmap();

        for(int i = 0; i < pix.getWidth(); i++)
        {
            for(int j = 0; j < pix.getHeight(); j++)
            {
                pixmap.setPixel(i, j, pix.getPixel(i, j));
            }
        }

        return pixmap;
    }

    public void setPixmap(Gdx2DPixmap pixmap) {
        this.pixmap = pixmap;
    }

    public Texture getTexture()
    {
        if(texture != null)
            return texture;

        texture = new Texture(new Pixmap(pixmap));
        return texture;
    }

    @Override
    public void dispose() {
        pixmap.dispose();
    }
}
