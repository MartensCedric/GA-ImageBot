package com.cedricmartens.imagebot;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;
import java.util.List;

public class ImageBot extends ApplicationAdapter {
	SpriteBatch batch;
	AssetManager assetManager;
	private Image originalImage;
	private List<Image> pop;
	@Override
	public void create () {
		batch = new SpriteBatch();
		assetManager = new AssetManager();

		assetManager.load("original.png", Texture.class);
		assetManager.finishLoading();

		originalImage = new Image((Texture) assetManager.get("original.png"));
		pop = new ArrayList<>();

		for(int i = 0; i < 10000; i++)
			pop.add(new Image(4, 4));

	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		doGeneration();
		batch.begin();
		batch.draw(originalImage.getTexture(), 0, 0, 400, 400);
		batch.draw(pop.get(0).getTexture(), 400, 0, 400, 400);
		batch.end();
	}

	private void doGeneration() {
		int f = getBestFitnessImage();
		for (int i = 0; i < pop.size(); i++) {
			pop.set(i, new Image(pop.get(f)));
		}
	}

	private int getBestFitnessImage()
	{
		int smallest = -1;
		float smallestDelta = Float.MAX_VALUE;

		for(int i = 0; i < pop.size(); i++)
		{
			float d = pop.get(i).getFitness(originalImage);

			if(d < smallestDelta) {
				smallestDelta = d;
				smallest = i;
			}
		}
		System.out.println(smallestDelta);
		return smallest;
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
