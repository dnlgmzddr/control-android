package com.banlinea.control;

import java.math.RoundingMode;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.banlinea.control.bussiness.BudgetService;
import com.banlinea.control.bussiness.CategoryService;
import com.banlinea.control.entities.Category;
import com.banlinea.control.entities.UserBudget;
import com.banlinea.control.remote.util.CallResult;

public class CategoryManagementActivity extends FragmentActivity {

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_category_management);
		// Show the Up button in the action bar.
		setupActionBar();

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());
		mSectionsPagerAdapter.registerDataSetObserver(new DataSetObserver() {

			@Override
			public void onChanged() {
				int currPage = mViewPager.getCurrentItem();
				mViewPager.setAdapter(mSectionsPagerAdapter);
				mViewPager.setCurrentItem(currPage);
			}

		});

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

	}

	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {

		getActionBar().setDisplayHomeAsUpEnabled(true);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.category_management, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			finish();
			return true;

		case R.id.add_category:
			/*
			 * Toast.makeText(this, "Create New Category", Toast.LENGTH_SHORT)
			 * .show();
			 */
			onCreateCategory();
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	void onCreateCategory() {
		int page = this.mViewPager.getCurrentItem();

		final int groupId = (page == 0 ? Category.GROUP_EXPENSE
				: (page == 1 ? Category.GROUP_INCOME : Category.GROUP_SAVING));

		AlertDialog.Builder builder = new AlertDialog.Builder(
				CategoryManagementActivity.this);

		LayoutInflater inflater = CategoryManagementActivity.this
				.getLayoutInflater();
		View layout = inflater.inflate(R.layout.alert_edit_category, null);
		builder.setTitle(R.string.edit_category_title);

		builder.setView(layout);
		final EditText categoryName = (EditText) layout
				.findViewById(R.id.category_name_textedit);
		final Spinner parentCategory = (Spinner) layout
				.findViewById(R.id.parent_spinner);
		ArrayAdapter<Category> adapter = new ArrayAdapter<Category>(
				CategoryManagementActivity.this, R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
		// adapter.add();
		CategoryService catService = new CategoryService(
				CategoryManagementActivity.this);
		List<Category> parentCategories = new ArrayList<Category>();
		try {
			parentCategories = catService.GetParentCategoriesPerGroup(groupId);
		} catch (SQLException e) {
			Log.d("CreateCategory", e.getMessage());
		}
		Category noCat = new Category();
		noCat.setName("Ninguna");
		noCat.setId("0");
		adapter.add(noCat);
		adapter.addAll(parentCategories);

		parentCategory.setAdapter(adapter);

		builder.setPositiveButton(R.string.confirm,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

						CallResult creationResult;
						if (!((Category) parentCategory.getSelectedItem())
								.getId().equals("0")) {
							creationResult = new CategoryService(
									CategoryManagementActivity.this)
									.AddCustomCategory(categoryName.getText()
											.toString(), groupId,
											((Category) parentCategory
													.getSelectedItem()).getId());
						} else {
							creationResult = new CategoryService(
									CategoryManagementActivity.this)
									.AddCustomCategory(categoryName.getText()
											.toString(), groupId);
						}

						if (creationResult.isSuccessfullOperation()) {
							Toast.makeText(
									CategoryManagementActivity.this,
									"Created: "
											+ categoryName.getText().toString(),
									Toast.LENGTH_SHORT).show();
							mViewPager.getAdapter().notifyDataSetChanged();
						} else {
							Toast.makeText(
									CategoryManagementActivity.this,
									"Error creating "
											+ categoryName.getText().toString()
											+ ": "
											+ creationResult.getMessage(),
									Toast.LENGTH_SHORT).show();
						}
					}
				});
		builder.setNegativeButton(R.string.cancel,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});
		AlertDialog dialog = builder.create();
		dialog.show();
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a DummySectionFragment (defined as a static inner class
			// below) with the page number as its lone argument.
			Fragment fragment = null;
			Bundle args;
			switch (position) {
			case 0:
				fragment = new ListSectionFragment();
				args = new Bundle();
				args.putInt(ListSectionFragment.ARG_SECTION_TYPE,
						Category.GROUP_EXPENSE);
				fragment.setArguments(args);
				break;
			case 1:
				fragment = new ListSectionFragment();
				args = new Bundle();
				args.putInt(ListSectionFragment.ARG_SECTION_TYPE,
						Category.GROUP_INCOME);
				fragment.setArguments(args);
				break;
			case 2:
				fragment = new ListSectionFragment();
				args = new Bundle();
				args.putInt(ListSectionFragment.ARG_SECTION_TYPE,
						Category.GROUP_SAVING);
				fragment.setArguments(args);
				break;
			}

			return fragment;
		}

		@Override
		public int getCount() {
			// Show 3 total pages.
			return 3;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.expenses_title).toUpperCase(l);
			case 1:
				return getString(R.string.incomes_title).toUpperCase(l);
			case 2:
				return getString(R.string.savings_title).toUpperCase(l);
			}
			return null;
		}

	}

	/**
	 * A list fragment representing a section of the app, but that simply
	 * displays a category list.
	 */
	public static class ListSectionFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		public static final String ARG_SECTION_TYPE = "section_type";
		private ExpandableListView categoryListView;
		private int groupId;

		public ListSectionFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(
					R.layout.fragment_category_management, container, false);

			groupId = getArguments().getInt(ARG_SECTION_TYPE);

			CategoryService catService = new CategoryService(getActivity());
			BudgetService budService = new BudgetService(getActivity());

			List<Category> parentCategories = new ArrayList<Category>();
			Map<String, List<Category>> childCategories = new HashMap<String, List<Category>>();
			Map<String, UserBudget> budgets = new HashMap<String, UserBudget>();
			// List<UserBudget> budgets = new ArrayList<UserBudget>();

			try {
				parentCategories = catService
						.GetParentCategoriesPerGroup(groupId);
				for (Category category : parentCategories) {

					budgets.put(category.getId(),
							budService.getUserBudget(category.getId()));
					List<Category> children = catService.GetChilds(category
							.getId());
					childCategories.put(category.getId(), children);
					for (Category category2 : children) {
						budgets.put(category2.getId(),
								budService.getUserBudget(category2.getId()));
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}

			final CategoryArrayAdapter adapter = new CategoryArrayAdapter(
					getActivity(), parentCategories, childCategories, budgets);

			categoryListView = (ExpandableListView) rootView
					.findViewById(R.id.listview);
			categoryListView.setAdapter(adapter);

			return rootView;
		}

		private class CategoryArrayAdapter extends BaseExpandableListAdapter {

			private final Context context;
			private List<Category> parentCategories;
			private Map<String, List<Category>> childCategories;
			private Map<String, UserBudget> budgets;

			public CategoryArrayAdapter(Context context,
					List<Category> parentCategories,
					Map<String, List<Category>> childCategories,
					Map<String, UserBudget> budgets) {
				super();
				this.context = context;
				this.parentCategories = parentCategories;
				this.childCategories = childCategories;
				this.budgets = budgets;
			}

			/*
			 * @Override public int getCount() { return categories.size(); }
			 * 
			 * @Override public Object getItem(int position) { return
			 * categories.get(position); }
			 * 
			 * @Override public long getItemId(int position) { return position;
			 * }
			 * 
			 * @Override public View getView(int position, View convertView,
			 * ViewGroup parent) { LayoutInflater inflater = (LayoutInflater)
			 * context .getSystemService(Context.LAYOUT_INFLATER_SERVICE); View
			 * rowView = inflater.inflate(R.layout.row_category_manager, parent,
			 * false);
			 * 
			 * final int pos = position; rowView.setOnClickListener(new
			 * View.OnClickListener() {
			 * 
			 * @Override public void onClick(View v) {
			 * /*Toast.makeText(getActivity().getApplicationContext(),
			 * "Click ListItem Number " + pos, Toast.LENGTH_LONG).show();
			 */
			/*
			 * AlertDialog.Builder builder = new AlertDialog.Builder(
			 * getActivity()); LayoutInflater inflater = getActivity()
			 * .getLayoutInflater(); View v1 = inflater.inflate(
			 * R.layout.alert_edit_category_budget, null); builder.setView(v1);
			 * final EditText budgetEditText = (EditText) v1
			 * .findViewById(R.id.budget); UserBudget ub = new
			 * BudgetService(getActivity
			 * ()).getUserBudget(categories.get(pos).getId()); if (ub != null) {
			 * budgetEditText.setText(""+ub.getBudget()); } else {
			 * budgetEditText.setText("0"); }
			 * builder.setTitle(getString(R.string.category_budget_title) + " "
			 * + categories.get(pos).getName());
			 * builder.setMessage(R.string.category_budget_message);
			 * builder.setPositiveButton(R.string.confirm, new
			 * DialogInterface.OnClickListener() {
			 * 
			 * @Override public void onClick(DialogInterface dialog, int which)
			 * {
			 * 
			 * UserBudget newb = new UserBudget();
			 * newb.setBudget(Float.parseFloat
			 * (budgetEditText.getText().toString())); newb.setIdUser(new
			 * AuthenticationService(getActivity()).GetUser().getId());
			 * newb.setIdCategory(categories.get(pos).getId()); // TODO: adjust
			 * period. newb.setPeriod(1); new
			 * BudgetService(getActivity()).AddBudget(newb);
			 * 
			 * /*Toast.makeText( getActivity(), "budget set to: $" +
			 * budgetEditText .getText() .toString(),
			 * Toast.LENGTH_SHORT).show();
			 */
			/*
			 * ((CategoryManagementActivity) getActivity()).mViewPager
			 * .getAdapter() .notifyDataSetChanged(); } });
			 * builder.setNegativeButton(R.string.cancel, new
			 * DialogInterface.OnClickListener() {
			 * 
			 * @Override public void onClick(DialogInterface dialog, int which)
			 * {
			 * 
			 * } }); AlertDialog dialog = builder.create(); dialog.show();
			 * 
			 * } });
			 * 
			 * TextView filledBarTV = (TextView) rowView
			 * .findViewById(R.id.filledBar); TextView emptyBarTV = (TextView)
			 * rowView .findViewById(R.id.emptyBar);
			 * 
			 * filledBarTV.setBackgroundColor(Color.GREEN);
			 * filledBarTV.setLayoutParams(new TableLayout.LayoutParams(0,
			 * android.view.ViewGroup.LayoutParams.MATCH_PARENT, 1f));
			 * emptyBarTV.setLayoutParams(new TableLayout.LayoutParams(0,
			 * android.view.ViewGroup.LayoutParams.MATCH_PARENT, 0f));
			 * UserBudget catBudget = null; for (UserBudget budget : budgets) {
			 * if
			 * (categories.get(position).getId().equals(budget.getIdCategory()))
			 * { catBudget = budget; break; } } if (catBudget != null) { float
			 * totalBudget = catBudget.getBudget(); float executedBudget =
			 * catBudget.getCurrentExecutedBudget(); float fraction =
			 * (totalBudget - executedBudget) / totalBudget; if (executedBudget
			 * > totalBudget) { filledBarTV.setBackgroundColor(Color.RED);
			 * filledBarTV.setLayoutParams(new TableLayout.LayoutParams(0,
			 * android.view.ViewGroup.LayoutParams.MATCH_PARENT, 0f));
			 * emptyBarTV.setLayoutParams(new TableLayout.LayoutParams(0,
			 * android.view.ViewGroup.LayoutParams.MATCH_PARENT, 1f)); } else {
			 * if (fraction < 0.5f) { if (fraction < 0.15f) {
			 * filledBarTV.setBackgroundColor(Color.RED); } else {
			 * filledBarTV.setBackgroundColor(Color.YELLOW); } }
			 * filledBarTV.setLayoutParams(new TableLayout.LayoutParams(0,
			 * android.view.ViewGroup.LayoutParams.MATCH_PARENT,
			 * 1.0f-fraction)); emptyBarTV.setLayoutParams(new
			 * TableLayout.LayoutParams(0,
			 * android.view.ViewGroup.LayoutParams.MATCH_PARENT, fraction)); }
			 * 
			 * TextView categoryNameTextView = (TextView) rowView
			 * .findViewById(R.id.category_name_textview); categoryNameTextView
			 * .setText(categories.get(position).getName() + " (" + fraction +
			 * "/" + totalBudget + ")");
			 * 
			 * } else { TextView categoryNameTextView = (TextView) rowView
			 * .findViewById(R.id.category_name_textview); categoryNameTextView
			 * .setText(categories.get(position).getName()); }
			 * 
			 * 
			 * 
			 * CategoryService catService = new CategoryService(getActivity()
			 * .getApplicationContext()); Category parentCategory; try {
			 * parentCategory = catService.GetCategory(categories.get(
			 * position).getIdParent()); TextView categoryParentTextView =
			 * (TextView) rowView .findViewById(R.id.parent_category_textview);
			 * if (parentCategory != null) { categoryParentTextView
			 * .setText(parentCategory.getName()); } else {
			 * categoryParentTextView.setText(""); } } catch (SQLException e) {
			 * Log.d(CategoryManagementActivity.class.getName(),
			 * e.getMessage()); }
			 * 
			 * ImageButton editButton = (ImageButton) rowView
			 * .findViewById(R.id.edit_imagebutton); ImageButton deleteButton =
			 * (ImageButton) rowView .findViewById(R.id.delete_imagebutton);
			 * 
			 * if (!categories.get(position).getIdOwner()
			 * .equals(Category.SYSTEM_OWNER_ID)) {
			 * 
			 * int page = ((CategoryManagementActivity)
			 * getActivity()).mViewPager .getCurrentItem();
			 * 
			 * final Category currCategory = categories.get(position);
			 * 
			 * final int groupId = (page == 0 ? Category.GROUP_EXPENSE : (page
			 * == 1 ? Category.GROUP_INCOME : Category.GROUP_SAVING));
			 * 
			 * editButton.setOnClickListener(new View.OnClickListener() {
			 * 
			 * @Override public void onClick(View v) {
			 * 
			 * AlertDialog.Builder builder = new AlertDialog.Builder(
			 * v.getContext());
			 * 
			 * LayoutInflater inflater = getActivity() .getLayoutInflater();
			 * View layout = inflater.inflate( R.layout.alert_edit_category,
			 * null); builder.setTitle(R.string.edit_category_title);
			 * builder.setView(layout); final EditText categoryName = (EditText)
			 * layout .findViewById(R.id.category_name_textedit);
			 * categoryName.setText(currCategory.getName()); final Spinner
			 * parentCategory = (Spinner) layout
			 * .findViewById(R.id.parent_spinner);
			 * 
			 * ArrayAdapter<Category> adapter = new ArrayAdapter<Category>(
			 * v.getContext(), R.layout.simple_spinner_item);
			 * adapter.setDropDownViewResource
			 * (R.layout.simple_spinner_dropdown_item);
			 * 
			 * CategoryService catService = new CategoryService(
			 * getActivity().getApplicationContext()); List<Category>
			 * parentCategories = new ArrayList<Category>(); try {
			 * parentCategories = catService
			 * .GetParentCategoriesPerGroup(groupId); } catch (SQLException e1)
			 * { e1.printStackTrace(); }
			 * 
			 * Category noCat = new Category(); noCat.setName("Ninguna");
			 * noCat.setId("0"); adapter.add(noCat);
			 * 
			 * adapter.addAll(parentCategories);
			 * parentCategory.setAdapter(adapter);
			 * 
			 * Log.d("Searching category", currCategory.getIdParent()); for (int
			 * i = 0; i < parentCategories.size(); i++) { Log.d("Looking at",
			 * parentCategories.get(i) .getId()); if
			 * (parentCategories.get(i).getId()
			 * .equals(currCategory.getIdParent())) {
			 * parentCategory.setSelection(i + 1); break; }
			 * 
			 * }
			 * 
			 * builder.setPositiveButton(R.string.confirm, new
			 * DialogInterface.OnClickListener() {
			 * 
			 * @Override public void onClick( DialogInterface dialog, int which)
			 * { CategoryService catService = new CategoryService( getActivity()
			 * .getApplicationContext()); List<Category> childrenCats = new
			 * ArrayList<Category>(); try { childrenCats = catService
			 * .GetChilds(currCategory .getId()); } catch (SQLException e) {
			 * e.printStackTrace(); } if (childrenCats.size() == 0) { CallResult
			 * editResult = null; if (((Category) parentCategory
			 * .getSelectedItem()) .getId().equals("0")) { editResult =
			 * catService.AddCustomCategory( currCategory .getId(), categoryName
			 * .getText() .toString(), groupId); } else { editResult =
			 * catService.AddCustomCategory( currCategory .getId(), categoryName
			 * .getText() .toString(), groupId, ((Category) parentCategory
			 * .getSelectedItem()) .getId()); } if (editResult
			 * .isSuccessfullOperation()) { /*Toast.makeText( getActivity()
			 * .getApplicationContext(), "Edited: " + categoryName .getText()
			 * .toString(), Toast.LENGTH_SHORT) .show();
			 *//*
				 * ((CategoryManagementActivity) getActivity()).mViewPager
				 * .getAdapter() .notifyDataSetChanged(); } else {
				 * Toast.makeText( getActivity() .getApplicationContext(),
				 * "Error: " + editResult .getMessage(), Toast.LENGTH_SHORT)
				 * .show(); } } else { Toast.makeText( getActivity()
				 * .getApplicationContext(),
				 * R.string.children_parent_category_error, Toast.LENGTH_SHORT)
				 * .show(); }
				 * 
				 * } }); builder.setNegativeButton(R.string.cancel, new
				 * DialogInterface.OnClickListener() {
				 * 
				 * @Override public void onClick( DialogInterface dialog, int
				 * which) { dialog.cancel(); } }); AlertDialog dialog =
				 * builder.create(); dialog.show();
				 * 
				 * /*Toast.makeText( getActivity().getApplicationContext(),
				 * "Edit", Toast.LENGTH_SHORT).show();
				 *//*
					 * } });
					 * 
					 * deleteButton.setOnClickListener(new
					 * View.OnClickListener() {
					 * 
					 * @Override public void onClick(View v) {
					 * 
					 * AlertDialog.Builder builder = new AlertDialog.Builder(
					 * v.getContext());
					 * builder.setTitle(R.string.delete_category_title)
					 * .setMessage( R.string.delete_category_message);
					 * builder.setPositiveButton(R.string.yes, new
					 * DialogInterface.OnClickListener() {
					 * 
					 * @Override public void onClick( DialogInterface dialog,
					 * int which) { Toast.makeText( getActivity()
					 * .getApplicationContext(), "Deleted not implemented yet.",
					 * Toast.LENGTH_SHORT).show(); } });
					 * builder.setNegativeButton(R.string.cancel, new
					 * DialogInterface.OnClickListener() {
					 * 
					 * @Override public void onClick( DialogInterface dialog,
					 * int which) { dialog.cancel(); } }); AlertDialog dialog =
					 * builder.create(); dialog.show(); } }); } else {
					 * editButton.setVisibility(View.INVISIBLE);
					 * deleteButton.setVisibility(View.INVISIBLE); }
					 * 
					 * return rowView; }
					 */
			@Override
			public Object getChild(int groupPosition, int childPosition) {
				return this.childCategories.get(
						this.parentCategories.get(groupPosition).getId()).get(
						childPosition);
			}

			@Override
			public long getChildId(int groupPosition, int childPosition) {
				return childPosition;
			}

			@Override
			public View getChildView(int groupPosition, int childPosition,
					boolean isLastChild, View convertView, ViewGroup parent) {

				final NumberFormat formatter = NumberFormat
						.getCurrencyInstance();
				formatter.setRoundingMode(RoundingMode.FLOOR);

				//if (convertView == null) {
					LayoutInflater inflater = (LayoutInflater) this.context
							.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					convertView = inflater.inflate(
							R.layout.row_category_manager, null);
					Category cat = this.childCategories.get(
							this.parentCategories.get(groupPosition).getId())
							.get(childPosition);
					UserBudget bud = this.budgets.get(cat.getName());

					TextView categoryNameTextView = (TextView) convertView
							.findViewById(R.id.category_name_textview);
					categoryNameTextView.setText(cat.getName());

					TextView budgetTextView = (TextView) convertView
							.findViewById(R.id.budget_textview);

					TextView filledBarTV = (TextView) convertView
							.findViewById(R.id.filledBar);

					TextView emptyBarTV = (TextView) convertView
							.findViewById(R.id.emptyBar);

					filledBarTV.setBackgroundColor(Color.GREEN);
					filledBarTV.setLayoutParams(new TableLayout.LayoutParams(0,
							android.view.ViewGroup.LayoutParams.MATCH_PARENT,
							1f));
					emptyBarTV.setLayoutParams(new TableLayout.LayoutParams(0,
							android.view.ViewGroup.LayoutParams.MATCH_PARENT,
							0f));

					if (bud != null) {
						budgetTextView.setText("Presupuesto: "
								+ formatter.format(bud.getBudget()));

						float totalBudget = bud.getBudget();
						float executedBudget = bud.getCurrentExecutedBudget();
						float percentage = executedBudget / totalBudget;

						filledBarTV
								.setLayoutParams(new TableLayout.LayoutParams(
										0,
										android.view.ViewGroup.LayoutParams.MATCH_PARENT,
										1f - percentage));
						emptyBarTV
								.setLayoutParams(new TableLayout.LayoutParams(
										0,
										android.view.ViewGroup.LayoutParams.MATCH_PARENT,
										percentage));

						if (percentage < 0.5f) {
							if (percentage < 0.15f) {
								filledBarTV.setBackgroundColor(Color.RED);
							} else {
								filledBarTV.setBackgroundColor(Color.YELLOW);
							}
						}

					} else {
						budgetTextView.setText("Presupuesto: "
								+ formatter.format(0));

					}

					ImageButton editButton = (ImageButton) convertView
							.findViewById(R.id.edit_imagebutton);
					ImageButton deleteButton = (ImageButton) convertView
							.findViewById(R.id.delete_imagebutton);
				//}
				return convertView;
			}

			@Override
			public int getChildrenCount(int groupPosition) {
				return this.childCategories.get(
						this.parentCategories.get(groupPosition).getId())
						.size();
			}

			@Override
			public Object getGroup(int groupPosition) {
				return this.parentCategories.get(groupPosition);
			}

			@Override
			public int getGroupCount() {
				return this.parentCategories.size();
			}

			@Override
			public long getGroupId(int groupPosition) {
				return groupPosition;
			}

			@Override
			public View getGroupView(int groupPosition, boolean isExpanded,
					View convertView, ViewGroup parent) {
				if (convertView == null) {
					LayoutInflater inflater = (LayoutInflater) this.context
							.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					convertView = inflater.inflate(
							R.layout.list_group_category_manager, null);

					TextView parentName = (TextView) convertView
							.findViewById(R.id.parent_category_name_textview);
					parentName.setText(parentCategories.get(groupPosition)
							.getName());
				}
				return convertView;
			}

			@Override
			public boolean hasStableIds() {
				return false;
			}

			@Override
			public boolean isChildSelectable(int groupPosition,
					int childPosition) {
				return true;
			}
		}
	}

}
