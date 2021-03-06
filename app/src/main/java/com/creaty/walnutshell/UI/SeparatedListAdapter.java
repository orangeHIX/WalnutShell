package com.creaty.walnutshell.UI;

import java.util.LinkedHashMap;
import java.util.Map;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;

public class SeparatedListAdapter extends BaseAdapter {
	public final Map<String, Adapter> sections = new LinkedHashMap<String, Adapter>();
	public final ArrayAdapter<String> headers;
	public boolean isSectionSelectable;
	public final static int TYPE_SECTION_HEADER = 0;

	public SeparatedListAdapter(ArrayAdapter<String> sectionAdapter,
			boolean isSecSele) {
		headers = sectionAdapter;
		isSectionSelectable = isSecSele;
	}

	public SeparatedListAdapter(Context context, int sectionView, int textView,
			boolean isSecSele) {
		this(new ArrayAdapter<String>(context, sectionView, textView),
				isSecSele);
	}

	public void addSection(String section, Adapter adapter) {
		this.headers.add(section);
		this.sections.put(section, adapter);
	}

	public void removeSection(String section) {
		this.headers.remove(section);
		this.sections.remove(section);
		// notifyDataSetChanged();
	}

	public String getSectionName(int position) {
		int headerIndex = 0;
		for (Object section : this.sections.keySet()) {
			Adapter adapter = sections.get(section);
			int size = adapter.getCount() + 1;

			// check if position inside this section
			if (position <= size)
				return headers.getItem(headerIndex);

			// otherwise jump into next section
			position -= size;
			headerIndex++;
		}
		return null;
	}

	public Adapter getSectionAdapter(String Section) {
		return sections.get(Section);
	}

	public Object getItem(int position) {
		for (Object section : this.sections.keySet()) {
			Adapter adapter = sections.get(section);
			int size = adapter.getCount() + 1;

			// check if position inside this section
			if (position == 0)
				return section;
			if (position < size)
				return adapter.getItem(position - 1);

			// otherwise jump into next section
			position -= size;
		}
		return null;
	}

	public int getCount() {
		// total together all sections, plus one for each section header
		int total = 0;
		for (Adapter adapter : this.sections.values())
			total += adapter.getCount() + 1;
		return total;
	}

	@Override
	public int getViewTypeCount() {
		// assume that headers count as one, then total all sections
		int total = 1;
		for (Adapter adapter : this.sections.values())
			total += adapter.getViewTypeCount();
		return total;
	}

	@Override
	public int getItemViewType(int position) {
		int type = 1;
		for (Object section : this.sections.keySet()) {
			Adapter adapter = sections.get(section);
			int size = adapter.getCount() + 1;

			// check if position inside this section
			if (position == 0)
				return TYPE_SECTION_HEADER;
			if (position < size)
				return type + adapter.getItemViewType(position - 1);

			// otherwise jump into next section
			position -= size;
			type += adapter.getViewTypeCount();
		}
		return -1;
	}

	public boolean areAllItemsSelectable() {
		return isSectionSelectable;
	}

	@Override
	public boolean isEnabled(int position) {
		if (isSectionSelectable) {
			return false;
		} else {
			return (getItemViewType(position) != TYPE_SECTION_HEADER);
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		int sectionnum = 0;
		for (Object section : this.sections.keySet()) {
			Adapter adapter = sections.get(section);
			int size = adapter.getCount() + 1;

			// check if position inside this section
			if (position == 0)
				return headers.getView(sectionnum, convertView, parent);
			if (position < size)
				return adapter.getView(position - 1, convertView, parent);

			// otherwise jump into next section
			position -= size;
			sectionnum++;
		}
		return null;
	}

	@Override
	public long getItemId(int position) {

		for (Object section : this.sections.keySet()) {
			Adapter adapter = sections.get(section);
			int size = adapter.getCount() + 1;

			// check if position inside this section
			if (position == 0)
				return 0;
			if (position < size)
				return adapter.getItemId(position - 1);

			// otherwise jump into next section
			position -= size;
		}
		return -1;
	}

}