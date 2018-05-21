package suan.mydns.jp.channel;

import suan.mydns.jp.Thoone;

public class Channel
{
	private String[] Text = {"1Ch\nSquare", "2Ch\nSquare", "3Ch\nTriangle", "4Ch\nNoise"};
	private int channel = 0;

	public void SetChannel(int Ch)
	{
		this.channel = Ch;
	}

	public int GetChannel()
	{
		return this.channel;
	}

	public void Draw(Thoone th)
	{
		for(int i = 0; i < 4; i++)
		{
			th.color(0);
			th.fill(255);
			if(this.channel == i) th.fill(0xFF, 0x00, 0x00);
			th.rect(0, 720/4*i, 1280/6, 720/4);
			th.fill(0);
			th.textSize(50);
			th.text(Text[i], 10, 720/4*i+300/4);
			if(th.kmState.MLeft)
			{
				if(th.kmState.IsMouseIn(0, 720/4*i, 1280/6, 720/4))
				{
					if(i < 3) th.mm2.ChStat(th.mm2.Sn[4][th.mm2.C], 0.5f, 16, 0, 1, false, 1, i);
					else th.mm2.ChStat(5, 0.0f, 16, 0, 1, false, 1, i);
					channel = i;
				}
			}
			else if(th.kmState.IsMouseIn(0, 720/4*i, 1280/6, 720/4))
			{
				if(i < 3) th.mm2.ChStat(-1, 0.5f, 16, 0, 1, false, 1, i);
				else th.mm2.ChStat(0, 0.5f, 16, 0, 1, false, 1, i);
			}
		}
	}
}
