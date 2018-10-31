package suan.mydns.jp.track;

import suan.mydns.jp.Thoone;
import suan.mydns.jp.music.MM2;

public class FourCh extends SuperTrack
{

	@Override
	public void Draw(Thoone th)
	{
		super.Draw(th);

		this.Onkai(th, 3);

		if(th.mv.enableMove) th.mv.move(th, this);

		boolean music = false;
		for(int i = 0; i < 4; i++)
		{
			th.fill(255);
			if(freq == i) th.fill(0xFF, 0x20, 0x00);
			if(MM2.old == i) th.fill(0xFF, 0x20, 0x00);
			th.rect(1280/12*(i+2), 0, 1280/12, 720/8);
			th.textSize(25);
			th.fill(0);
			th.text(Dutys[1][i], 1280/12*(i+2)+20, 720/8/2+10);
			if(th.kmState.MLeft)
			{
				if(th.kmState.IsMouseIn(1280/12*(i+2), 0, 1280/12, 720/8))
				{
					if(i < 2)
					{
						music = true;
						freq = i;
						th.mm2.ChStat(5, fr[1][i], 15, 0, 1, false, 1, 3, 16);
					}
					else
					{
						MM2.old = i;
					}
				}
			}
		}
		if(!music && th.kmState.IsMouseIn(1280/6, 0, 1280/6*2, 720/8))
		{
			if(!th.musics.start) th.mm2.ChStat(0, 0.0f, 16, 0, 1, false, -1, 3, 16);
		}

		this.Shift(th);
	}

	@Override
	protected void Onkai(Thoone th, int Channel)
	{
		boolean music = false;

		for(int j = 0; j < 10; j++)
		{
			for(int k = -1; k < 6; k++)
			{
				th.fill(0xF0, 0xF0, 0xF0);
				th.rect(1280/6*(k+1)+60-Math.abs(this.ShiftX)%(1280/6), (j+1)*12*-20+this.ShiftY, 1280/6, 12*20);
				th.fill(0);
				th.textSize(20);
				th.text(Math.abs(this.ShiftX/(1280/6))+k, 1280/6*(k+1)+60+this.ShiftX%(1280/6), 720/4+20);
			}
		}

		this.Music(th);

		th.fill(0x00, 0xFF, 0x00);
		th.rect(1280/6, 720/4, 60, 720/4*3);

		for(int j = 0; j < 10; j++)
		{
			for(int i = 0; i < 12; i++)
			{
				th.fill(0);
				th.textSize(20);
				th.text(i + j*12, 1280/6, (i+j*12)*-20+this.ShiftY);
				if(th.kmState.MLeft && (i+1+j*12)*-20+this.ShiftY + 20 > 720 / 4)
				{
					if(th.kmState.IsMouseIn(1280/6, (i+1+j*12)*-20+this.ShiftY, 1280/6*5, 20))
					{
						music = true;
						th.mm2.ChStat(((i + j * 12) > 0 && (i + j * 12) < 17) ? th.mm2.SnN[i + j * 12 - 1] : 0, fr[(Channel == 0 ? 0 : Channel == 1 ? 0 : 1)][freq], this.Vol, this.VolD, this.FreD, true, temp, Channel, this.VDUM);
						//if((i + j * 12) > 0 && (i + j * 12) < 17)System.out.println(i + " " + j + " " + th.mm2.SnN[i + j * 12 - 1]);
						if(!th.mv.enableMove) this.Norts(th, i, j, Channel);
					}
				}
				if(th.kmState.MRight && (i+1+j*12)*-20+this.ShiftY + 20 > 720 / 4)
				{
					if(th.kmState.IsMouseIn(1280/6, (i+1+j*12)*-20+this.ShiftY, 1280/6*5, 20))
					{
						if(!th.mv.enableMove) this.Norts(th, i, j, Channel);
					}
				}
			}
		}
		if(!music && th.kmState.IsMouseIn(1280/6, 720/4, 1280/6*5, 750/4*3))
		{
			this.Clicked = false;
			temp = R.nextInt(50)+2;
			if(!th.musics.start) th.mm2.ChStat(Channel == 3 ? 0 : -1, 0.5f, this.Vol, this.VolD, this.FreD, true, temp, Channel, this.VDUM);
		}

		th.fill(255);
		th.rect(1280/6, 0, 1280/6*5, 720/8*2);

		this.Onpu(th);
	}
}
