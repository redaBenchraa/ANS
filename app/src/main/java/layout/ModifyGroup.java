package layout;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.reda_benchraa.asn.DAO.Utility;
import com.example.reda_benchraa.asn.Model.Group;
import com.example.reda_benchraa.asn.R;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;


public class ModifyGroup extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "group";

    // TODO: Rename and change types of parameters
    static int PICK_PHOTO_FOR_GROUP = 1;
    Group group;
    String id;
    ImageView image;
    EditText name,about;
    Button save;


    //private OnFragmentInteractionListener mListener;

    public ModifyGroup() {
        // Required empty public constructor
    }

    public static Fragment newInstance(String id) {
        ModifyGroup fragment = new ModifyGroup();
        Bundle args = new Bundle();
        args.putSerializable("group",id);
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
        return inflater.inflate(R.layout.fragment_modify_group, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View v = getView();
        if(v != null){
            name = (EditText) v.findViewById(R.id.modifyGroup_name);
            about = (EditText) v.findViewById(R.id.modifyGroup_about);
            image = (ImageView) v.findViewById(R.id.modifyGroup_image);
            save = (Button) v.findViewById(R.id.modifyGroup_save);
            if (getArguments() != null) {
                id = getArguments().getString(ARG_PARAM1);
                getGroup(getContext(), new HashMap<String, String>(), Utility.getProperty("API_URL",getContext())+"Groups/"+id);
                image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(i, PICK_PHOTO_FOR_GROUP);
                    }
                });
                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Map map = new HashMap<String, String>();
                        map.put("Name",name.getText().toString());
                        map.put("About",about.getText().toString());
                        if(group.getImage()!=null){
                            String encodedImage = Base64.encodeToString(group.getImage(), Base64.DEFAULT);
                            map.put("Image",encodedImage);
                        }
                        updateGroup(getContext(), map, Utility.getProperty("API_URL",getContext())+"Groups/"+group.getId());

                    }
                });
            }
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_PHOTO_FOR_GROUP && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                Toast.makeText(getContext(), "No photo selected", Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                InputStream inputStream = getContext().getContentResolver().openInputStream(data.getData());
                Uri selectedImage = data.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };
                Cursor cursor = getContext().getContentResolver().query(selectedImage,filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                cursor.close();
                group.setImage(IOUtils.toByteArray(inputStream));
                image.setImageBitmap(BitmapFactory.decodeFile(picturePath));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private void updateGroup(final Context context,final Map<String, String> map,final String url) {
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest sr = new StringRequest(Request.Method.PUT,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(context, "Changes Saved", Toast.LENGTH_SHORT).show();
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
                    name.setText(group.getName());
                    about.setText(group.getAbout());
                    if(group.getImage()!=null) {
                        image.setImageBitmap(BitmapFactory.decodeByteArray(group.getImage(), 0, group.getImage().length));
                    }
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
}
