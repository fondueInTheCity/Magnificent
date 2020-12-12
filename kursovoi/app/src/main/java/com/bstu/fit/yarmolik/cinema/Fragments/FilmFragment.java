package com.bstu.fit.yarmolik.cinema.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bstu.fit.yarmolik.cinema.CheckInternetConnection;
import com.bstu.fit.yarmolik.cinema.Login;
import com.bstu.fit.yarmolik.cinema.ModelAdapter.FilmModel;
import com.bstu.fit.yarmolik.cinema.R;
import com.bstu.fit.yarmolik.cinema.RecyclerViewAdapter.RecyclerViewAdapterFilms;
import com.bstu.fit.yarmolik.cinema.Remote.IMyApi;
import com.bstu.fit.yarmolik.cinema.Remote.RetrofitClient;
import com.bstu.fit.yarmolik.cinema.Responces.FilmResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FilmFragment extends Fragment {
private IMyApi iMyApi;
private RecyclerView recyclerView;
private RecyclerViewAdapterFilms recyclerViewAdapterFilms;
private ArrayList<FilmModel> film;
private List<FilmResponse> posts;
private ArrayList<String> nameList;
private ArrayList<String> idList;
private  Integer roleId;
private CheckInternetConnection checkInternetConnection;
public boolean checkInternetState;
private ArrayList<String> posterList;
private ArrayList<String> descriptionList;
private ArrayList<Integer> durationList;
private ArrayList<Integer> yearList;
private ArrayList<String> genreList;
private ArrayList<String> countryList;
private ArrayList<Float> ratingList;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_film, container, false);
        init(view);
        checkInternetConnection=new CheckInternetConnection();
        checkInternetConnection.installListener(getContext());
            checkInternetState=true;
            recyclerView.setVisibility(View.VISIBLE);
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
            loadFilmsInfo();
        return view;
    }
    private void init(View view){
        recyclerView=view.findViewById(R.id.recyclerFilmView);
        roleId= Login.userRoleId;
        iMyApi= RetrofitClient.getInstance().create(IMyApi.class);
        nameList= new ArrayList<String>();
        idList=new ArrayList<String>();
        posterList=new ArrayList<String>();
        descriptionList=new ArrayList<String>();
        durationList=new ArrayList<Integer>();
        yearList=new ArrayList<Integer>();
        genreList=new ArrayList<String>();
        countryList=new ArrayList<String>();
        film=new ArrayList<>();
        ratingList=new ArrayList<>();
    }
    public void loadFilmsInfo(){

        Call<List<FilmResponse>> call=iMyApi.getFilms();
        call.enqueue(new Callback<List<FilmResponse>>() {
            @Override
            public void onResponse(Call<List<FilmResponse>> call, Response<List<FilmResponse>> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(getContext(), response.code(), Toast.LENGTH_LONG).show();
                }
                posts=response.body();
                for(FilmResponse post : posts){
                    nameList.add(post.getName());
                    idList.add(post.getId());
                    countryList.add(post.getCountry());
                    genreList.add((post.getGenre()));
                    descriptionList.add(post.getDescription());
                    durationList.add(post.getDuration());
                    posterList.add(post.getPoster());
                    yearList.add(post.getYear());
                    ratingList.add(post.getRating());
                }
                for(int i=0;i<posterList.size();i++){
                    try {
                        film.add(new FilmModel(nameList.get(i), genreList.get(i), ratingList.get(i),posterList.get(i),idList.get(i)));//getBitmapFromURL(posterList.get(i))));
                    }
                    catch (Exception ex){
                        Toast.makeText(getContext(),ex.getMessage()+" Exception",Toast.LENGTH_SHORT).show();
                    }
                }
                recyclerViewAdapterFilms = new RecyclerViewAdapterFilms(getContext(),film);
                recyclerView.setAdapter(recyclerViewAdapterFilms);
            }
            @Override
            public void onFailure(Call<List<FilmResponse>> call, Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
