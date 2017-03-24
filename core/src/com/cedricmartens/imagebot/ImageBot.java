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
	private Image bestImage;
	private float allTimeBest;
	private List<Image> pop;
	@Override
	public void create () {
		batch = new SpriteBatch();
		assetManager = new AssetManager();

		assetManager.load("original.png", Texture.class);
		assetManager.finishLoading();

		Texture texture = assetManager.get("original.png");
		originalImage = new Image(texture);
		pop = new ArrayList<>();
		allTimeBest = Float.MAX_VALUE;
		for(int i = 0; i < 10000; i++)
			pop.add(new Image(texture.getWidth(), texture.getHeight()));

	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		doGeneration();
		batch.begin();
		batch.draw(originalImage.getTexture(), 0, 0, 400, 400);
		batch.draw(bestImage.getTexture(), 400, 0, 400, 400);
		batch.draw(pop.get(0).getTexture(), 800, 0, 400, 400);
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


		if(smallestDelta < allTimeBest)
		{
			bestImage = pop.get(smallest);
			allTimeBest = bestImage.getFitness(originalImage);
			System.out.println("New high " + allTimeBest);
		}

		return smallest;
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
