package suan.mydns.jp.play;

import processing.core.PApplet;
import suan.mydns.jp.Thoone;
import suan.mydns.jp.music.MM2;

public class PlayScreen extends PApplet
{
	public static boolean Visible = true;
	public static Thoone th;
	boolean keypressed = false;
	boolean playscreen = false;
	boolean pressed = false;

	@Override
	public void settings()
	{
		size(300, 250);
	}

	@Override
	public void setup()
	{
		th.ps = this;
		/*String str[] = PFont.list();
		for(String a : str)
		{
			System.out.println(a);
		}//*/
		try
		{
			this.textFont(this.createFont("ＭＳ ゴシック", 16));
		}
		catch(Exception e)
		{
		}
	}

	int temp[] = {0xFFFF, 0xFFFF, 0xFFFF, 0xFFFF, 0xFFFF};
	double temp2[] = {0xFFFF, 0xFFFF, 0xFFFF, 0xFFFF, 0xFFFF};

	@Override
	public void draw()
	{
		if(this.Visible && th != null)
		{
			/*this.fill(255);
			this.rect(0, 0, 1020, 600);//*/
			boolean rewrite = false;
			for(int i = 0; i < 5; i++)
			{
				if(temp[i] != (int)(MM2.Volumes[i] + (int)(MM2.Volumes[i] + ((int)((MM2.Dutys[i] >= 1.0 ? MM2.Dutys[i/2] / 4.0 : MM2.Dutys[i]) * 4) << 8)) + (th.musics.nowNotes[i] << 16))) rewrite = true;
				if(temp2[i] != MM2.Frequencyss[i]) rewrite = true;
			}
			if(rewrite)
			{
				background(255);
				this.fill(0);

				this.fill(0);
				this.textSize(20);
				for(int i = 0; i < 10; i++)
				{
					if(i % 2 == 0)
					{
						try
						{
							if(th.musics.nowNotes[i/2] < th.SPT[i/2].Freque.size())
							{
								temp[i/2] = (int)(Math.max(Math.min(MM2.Volumes[i/2], MM2.VDown[i/2] <= 0 ? 16 : MM2.MVolDUM[i/2]), MM2.VDown[i/2] >= 0 ? 0 : MM2.MVolDUM[i/2]) + ((int)((MM2.Dutys[i/2] >= 1.0 ? MM2.Dutys[i/2] / 4.0 : MM2.Dutys[i/2]) * 4) << 8) + (th.musics.nowNotes[i/2] << 16));

								this.text(String.format("Channel " + (i/2 + 1) +" : %08x", temp[i/2]).toUpperCase(), 20, 20 * (i + 1));
							}
							else
							{
								this.text(String.format("Channel " + (i/2 + 1) +" : %08x", temp[i/2]).toUpperCase(), 20, 20 * (i + 1));
							}
						}
						catch(IndexOutOfBoundsException e)
						{
							e.printStackTrace();
						}
					}
					else
					{
						try
						{
							if(th.musics.nowNotes[i/2] < th.SPT[i/2].Freque.size())
							{
								int ChangeRate = 0;
								if(i / 2 < 3) ChangeRate = (int)(MM2.FamicomHz / MM2.Frequencyss[i/2] + 0.5)/(i / 2 == 2 ? 2 : 1);
								else ChangeRate = (int)(th.SPT[i/2].FrequI.get(th.musics.nowNotes[i/2]));// + 16 * th.SPT[i/2].Duty.get(th.musics.nowNotes[i/2]));
								if(th.SPT[i/2].Time.get(th.musics.nowNotes[i/2]) > th.musics.time) ChangeRate = 0;
								this.text(String.format("Channel " + (i/2 + 1) +" : %04x", ChangeRate == 0x7FFFFFFF ? 0 : ChangeRate > 0xFFFF ? 0xFFFF : ChangeRate).toUpperCase(), 20, 20 * (i + 1));
							}
							else
							{
								this.text(String.format("Channel " + (i/2 + 1) +" : %04x", 0).toUpperCase(), 20, 20 * (i + 1));
							}
						}
						catch(IndexOutOfBoundsException e)
						{
							e.printStackTrace();
						}
					}
				}
			}

			if(!pressed && th.kmState.KeyC.get(' '))
			{
				pressed = true;
				th.musics.start = !th.musics.start;
				if(!th.musics.start)
				{
					for(int i = 0; i < 5; i++)
					{
						//System.out.print(th.musics.nowNotes[i] + " ");
						th.musics.nowNotes[i] = 0;
						//System.out.println(th.musics.nowNotes[i]);
					}
					th.mm2.resk = true;
					th.mm2.resktime = System.currentTimeMillis();
				}
			}
			else if(pressed && !th.kmState.KeyC.get(' '))
			{
				for(int i = 0; i < 5; i++)
				{
					th.mm2.ChStat(i == 3 ? 0 : -1, 0.5f, 16, 0.0f, 1.0f, true, -1, i, 16);
				}
				pressed = false;
			}
			if(th.mm2.resk && System.currentTimeMillis() - th.mm2.resktime > 100) th.mm2.resk = false;

			/*if(th.kmState.Key.get(116) && !keypressed)
			{
				keypressed = true;
				this.changeVisible();
				th.changeVisible();
				if(Visible) th.kmState.Key.put(115, false);
			}
			else if(!th.kmState.Key.get(116) && keypressed)
			{
				keypressed = false;
			}//*/
		}
	}

	public void changeVisible()
	{
		Visible = ! Visible;
		getSurface().setVisible(Visible);
	}

	@Override
	public void mousePressed()
	{
		if( mouseButton == LEFT )
		{
			//左ボタンが押されたときの処理
			th.kmState.MLeft = true;
		}
		else if( mouseButton == CENTER )
		{
			//中央ボタンが押されたときの処理
			th.kmState.MCenter = true;
		}
		else if( mouseButton == RIGHT )
		{
			//右ボタンが押されたときの処理
			th.kmState.MRight = true;
		}
	}

	@Override
	public void mouseReleased()
	{
		if( mouseButton == LEFT )
		{
			//左ボタンが押されたときの処理
			th.kmState.MLeft = false;
		}
		else if( mouseButton == CENTER )
		{
			//中央ボタンが押されたときの処理
			th.kmState.MCenter = false;
		}
		else if( mouseButton == RIGHT )
		{
			//右ボタンが押されたときの処理
			th.kmState.MRight = false;
		}
	}


	@Override
	public void keyPressed()
	{
		th.kmState.Key.put(keyCode, true);
		th.kmState.KeyC.put(key, true);
		//System.out.println(this.key + " " + this.keyCode);
	}

	@Override
	public void keyReleased()
	{
		th.kmState.Key.put(keyCode, false);
		th.kmState.KeyC.put(key, false);
	}
}
