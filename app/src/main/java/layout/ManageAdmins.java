package layout;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.reda_benchraa.asn.Adapters.accountArrayAdapter;
import com.example.reda_benchraa.asn.DAO.Utility;
import com.example.reda_benchraa.asn.Model.Account;
import com.example.reda_benchraa.asn.Model.Group;
import com.example.reda_benchraa.asn.Profile;
import com.example.reda_benchraa.asn.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ManageAdmins extends Fragment {
    private static final String ARG_PARAM1 = "group";
    private Group group;
    String id;
    ListView admins;
    accountArrayAdapter adminsAdapter;
    List<Account> adminList = new ArrayList<>();

    //private OnFragmentInteractionListener mListener;

    public ManageAdmins() {
        // Required empty public constructor
    }


    public static ManageAdmins newInstance(String id) {
        ManageAdmins fragment = new ManageAdmins();
        Bundle args = new Bundle();
        args.putString("group", id);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_manage_admins, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View v = getView();
        if(v != null){
            admins = (ListView) v.findViewById(R.id.listView_manageAdmins);
            if (getArguments() != null) {
                id = getArguments().getString(ARG_PARAM1);
                getGroup(getContext(), new HashMap<String, String>(), Utility.getProperty("API_URL",getContext())+"Groups/"+id+"/?include=admins");
            }
            admins.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Account account = (Account) parent.getAdapter().getItem(position);;
                    Toast.makeText(getContext(), account.getEmail(), Toast.LENGTH_SHORT).show();
                    Intent t = new Intent(view.getContext(),Profile.class);
                    t.putExtra("account",account);
                    startActivityForResult(t,100);


                }
            });
            admins.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                    final Dialog dialog = new Dialog(getContext());
                    dialog.setContentView(R.layout.admin_dialogue);
                    TextView text = (TextView) dialog.findViewById(R.id.adminDialogue_name);
                    final Account account = (Account) parent.getAdapter().getItem(position);;
                    text.setText(account.getFirstName() + " " + account.getLastName() );
                    Button dialogButton = (Button) dialog.findViewById(R.id.adminDialogue_delete);
                    Button dialogButtonNo = (Button) dialog.findViewById(R.id.adminDialogue_admin);
                    dialogButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Map map = new HashMap<String,String>();
                            adminsAdapter.remove(account);
                            adminsAdapter.notifyDataSetChanged();
                            map.put("memberId",Long.toString(account.getId()));
                            deleteMember(getContext(),map, Utility.getProperty("API_URL",getContext())+"Groups/"+group.getId()+"/removeMember");
                            map.put("adminId",Long.toString(account.getId()));
                            removeAdmin(getContext(),map, Utility.getProperty("API_URL",getContext())+"Groups/"+group.getId()+"/removeAdmin");
                            dialog.dismiss();
                        }
                    });
                    dialogButtonNo.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Map map = new HashMap<String,String>();
                            map.put("adminId",Long.toString(account.getId()));
                            Toast.makeText(getContext(), account.getFirstName()+" "+position, Toast.LENGTH_SHORT).show();
                            adminsAdapter.remove(account);
                            adminsAdapter.notifyDataSetChanged();
                            removeAdmin(getContext(),map, Utility.getProperty("API_URL",getContext())+"Groups/"+group.getId()+"/removeAdmin");                            dialog.dismiss();
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                    return true;
                }
            });

        }
    }
    private void deleteMember(final Context context, final Map<String, String> map, final String url) {
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest sr = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(context, "Member removed", Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String,String> getParams(){
                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return Utility.getMap();
            }
        };
        queue.add(sr);
    }
    private void removeAdmin(final Context context, final Map<String, String> map, final String url) {
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest sr = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(context, "Admin removed", Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String,String> getParams(){
                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return Utility.getMap();
            }
        };
        queue.add(sr);
    }
    private void getGroup(final Context context,final Map<String, String> map,final String url) {
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest sr = new StringRequest(Request.Method.GET,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    group = Group.mapJson((JSONObject) new JSONArray(response).get(0));
                    adminList.clear();
                    adminList.addAll(group.getAdmins());
                    adminsAdapter = new accountArrayAdapter(getContext(), R.layout.account_item, adminList);
                    admins.setAdapter(adminsAdapter);
                } catch (JSONException | ParseException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String,String> getParams(){
                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return Utility.getMap();
            }
        };
        queue.add(sr);
    }
    /*
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
    */
    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    /*public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }*/
}
