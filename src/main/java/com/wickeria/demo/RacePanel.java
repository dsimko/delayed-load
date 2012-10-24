package com.wickeria.demo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.RefreshingView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;

public class RacePanel extends Panel {

	private static final long serialVersionUID = 1L;

	public RacePanel(String id) {
		super(id);
		add(new RefreshingView<RaceRow>("rows") {
			private static final long serialVersionUID = 1L;

			@Override
			protected Iterator<IModel<RaceRow>> getItemModels() {
				List<IModel<RaceRow>> rowsModel = new ArrayList<IModel<RaceRow>>();
				for (RaceRow raceRow : prepareRaceRows()) {
					rowsModel.add(new CompoundPropertyModel<RaceRow>(raceRow));
				}
				return rowsModel.iterator();
			}

			@Override
			protected void populateItem(final Item<RaceRow> item) {
				item.add(new Label("id"));
				item.add(new Label("year"));
				item.add(new RacePhotosPanel("photos", item.getModel()));
			}
		});
	}

	private static List<RaceRow> prepareRaceRows() {
		List<RaceRow> rows = new ArrayList<RaceRow>();
		for (int i = 1; i < 6; i++) {
			rows.add(new RaceRow(Long.valueOf(i), (short) (2012 - i)));
		}
		return rows;
	}

	public static class RaceRow implements Serializable {

		private static final long serialVersionUID = 1L;
		private final Long id;
		private final short year;

		public RaceRow(Long id, short year) {
			this.id = id;
			this.year = year;
		}

		public Long getId() {
			return id;
		}

		public short getYear() {
			return year;
		}
	}
}
