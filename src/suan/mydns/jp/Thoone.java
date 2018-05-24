package suan.mydns.jp;

import processing.core.PApplet;
import suan.mydns.jp.channel.Channel;
import suan.mydns.jp.io.SaveLoad;
import suan.mydns.jp.music.MM2;
import suan.mydns.jp.music.MSTART;
import suan.mydns.jp.state.DeleteOverlap;
import suan.mydns.jp.state.KMState;
import suan.mydns.jp.state.Sort;
import suan.mydns.jp.track.FourCh;
import suan.mydns.jp.track.OneCh;
import suan.mydns.jp.track.SuperTrack;
import suan.mydns.jp.track.ThreeCh;
import suan.mydns.jp.track.TwoCh;

public class Thoone extends PApplet
{
	public MM2 mm2 = new MM2();
	public KMState kmState = new KMState(this);
	public Channel ch = new Channel();
	public SuperTrack[] SPT = new SuperTrack[4];
	public MSTART musics = new MSTART();
	public Sort sort = new Sort();
	public SaveLoad sl = new SaveLoad();
	public DeleteOverlap DO = new DeleteOverlap();
	private boolean pressed = false;

	public static void main(String[] args)
	{
		// TODO 自動生成されたメソッド・スタブ
		PApplet.main("suan.mydns.jp.Thoone");
	}

	@Override
	public void settings()
	{
		size(1280, 720);
	}

	@Override
	public void setup()
	{
		SPT[0] = new OneCh();
		SPT[1] = new TwoCh();
		SPT[2] = new ThreeCh();
		SPT[3] = new FourCh();
	}

	@Override
	public void draw()
	{
		fill(255);
		rect(0, 0, 1280, 720);
		SPT[ch.GetChannel()].Draw(this);
		sort.Draw(this);
		DO.Draw(this);
		sl.Draw(this);
		ch.Draw(this);
		musics.Play(this);
		kmState.Mouse[0] = this.mouseX;
		kmState.Mouse[1] = this.mouseY;
		if(!pressed && this.kmState.KeyC.get(' '))
		{
			pressed = true;
			this.musics.start = !this.musics.start;
			if(!this.musics.start)
			{
				for(int i = 0; i < 4; i++)
				{
					this.musics.nowNotes[i] = 0;
				}
			}
		}
		else if(pressed && !this.kmState.KeyC.get(' '))
		{
			for(int i = 0; i < 4; i++)
			{
				this.mm2.ChStat(i == 3 ? 0 : -1, 0.5f, 16, 0.0f, 1.0f, true, 0, i);
			}
			pressed = false;
		}
	}

	@Override
	public void mousePressed()
	{
		if( mouseButton == LEFT )
		{
			//左ボタンが押されたときの処理
			kmState.MLeft = true;
		}
		else if( mouseButton == CENTER )
		{
			//中央ボタンが押されたときの処理
			kmState.MCenter = true;
		}
		else if( mouseButton == RIGHT )
		{
			//右ボタンが押されたときの処理
			kmState.MRight = true;
		}
	}

	@Override
	public void mouseReleased()
	{
		if( mouseButton == LEFT )
		{
			//左ボタンが押されたときの処理
			kmState.MLeft = false;
		}
		else if( mouseButton == CENTER )
		{
			//中央ボタンが押されたときの処理
			kmState.MCenter = false;
		}
		else if( mouseButton == RIGHT )
		{
			//右ボタンが押されたときの処理
			kmState.MRight = false;
		}
	}


	@Override
	public void keyPressed()
	{
		kmState.Key.put(keyCode, true);
		kmState.KeyC.put(key, true);
	}

	@Override
	public void keyReleased()
	{
		kmState.Key.put(keyCode, false);
		kmState.KeyC.put(key, false);
	}
}
