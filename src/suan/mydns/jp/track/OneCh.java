package suan.mydns.jp.track;

import suan.mydns.jp.Thoone;

public class OneCh extends SuperTrack
{
	@Override
	public void Draw(Thoone th)
	{
		super.Draw(th);

		this.Onkai(th, 0);

		boolean music = false;
		for(int i = 0; i < 4; i++)
		{
			th.fill(255);
			if(freq == i) th.fill(0xFF, 0x20, 0x00);
			th.rect(1280/6*(i+1), 0, 1280/6, 720/8);
			th.textSize(25);
			th.fill(0);
			th.text(Dutys[0][i], 1280/6*(i+1)+20, 720/8/2+10);
			if(th.kmState.MLeft)
			{
				if(th.kmState.IsMouseIn(1280/6*(i+1), 0, 1280/6, 720/8))
				{
					music = true;
					freq = i;
					th.mm2.ChStat(th.mm2.Sn[4][th.mm2.C], fr[0][i], 16, 0, 1, false, 1, 0);
				}
			}
		}
		if(!music && th.kmState.IsMouseIn(1280/6, 0, 1280/6*4, 720/8))
		{
			th.mm2.ChStat(-1, 0.5f, 16, 0, 1, false, 1, 0);
		}

		this.Shift(th);
	}
}
