/**
 * Create a bipartite graph from a matrix. Each row corresponds to vertex
 * with the same id as the row number (0-based), but vertices correponsing to columns
 * have id + num-rows.
 * Line format of the type
 * [user] [item] [T/weight] [rating]
 */

template <typename als_edge_type>
int convert_matrixmarket_square_4(std::string base_filename, int type = TRAINING, int matlab_square_offset = 1) {
    // Note, code based on: http://math.nist.gov/MatrixMarket/mmio/c/example_read.c
    FILE *f = NULL;
    size_t nz;
    /**
     * Create sharder object
     */
    int nshards;
    if ((nshards = find_shards<als_edge_type>(base_filename, get_option_string("nshards", "auto")))) {

        if (check_origfile_modification_earlier<als_edge_type>(base_filename, nshards)) {
            logstream(LOG_INFO) << "File " << base_filename << " was already preprocessed, won't do it again. " << std::endl;
            read_global_mean(base_filename, type);
        }
        return nshards;
    }

    sharder<als_edge_type> sharderobj(base_filename);
    sharderobj.start_preprocessing();


    detect_matrix_size(base_filename, f, type == TRAINING? M:Me, type == TRAINING? N:Ne, nz);
    if (f == NULL){
        if (type == VALIDATION){
            logstream(LOG_INFO)<< "Did not find validation file: " << base_filename << std::endl;
            return -1;
        }
        else if (type == TRAINING)
            logstream(LOG_FATAL)<<"Failed to open training input file: " << base_filename << std::endl;
    }

    compute_matrix_size(nz, type);

    uint I, J, T;
    double val;
    bool active_edge = true;

    for (size_t i=0; i<nz; i++)
    {
        int rc = fscanf(f, "%d %d %d %lg\n", &I, &J, &T, &val);
        if (rc != 4)
            logstream(LOG_FATAL)<<"Error when reading input file - line " << i << std::endl;
        if (T < 0)
            logstream(LOG_FATAL)<<"T (third columns) should be >= 0 " << std::endl;
        I-=input_file_offset;  /* adjust from 1-based to 0-based */
        J-=input_file_offset;
        if (I >= M)
            logstream(LOG_FATAL)<<"Row index larger than the matrix row size " << I << " > " << M << " in line: " << i << std::endl;
        if (J >= N)
            logstream(LOG_FATAL)<<"Col index larger than the matrix col size " << J << " > " << N << " in line; " << i << std::endl;
        K = std::max((int)T, (int)K);
        T -= matlab_square_offset;
        if (T < 0)
            logstream(LOG_FATAL)<<"T bins should be >= 1 in row " << i << std::endl;
        //avoid self edges
        if (I == J)
            continue;

        active_edge = decide_if_edge_is_active(i, type);

        if (active_edge){
            if (type == TRAINING)
                globalMean += val;
            else globalMean2 += val;
        }
        if (T == 1) {
            if (val > 24) val = 24;
            val = (val-10)/10;
        }
        sharderobj.preprocessing_add_edge(I, J + T*M, als_edge_type(val));
        //sharderobj.preprocessing_add_edge(J + T*M, M+M+T, als_edge_type(val));
    }

    if (type == TRAINING){
        uint toadd = 0;
        if (implicitratingtype == IMPLICIT_RATING_RANDOM)
            toadd = add_implicit_edges4(implicitratingtype, sharderobj); // TODO: removed weight here
        globalMean += implicitratingvalue * toadd;
        L += toadd;
        globalMean /= L;
        logstream(LOG_INFO) << "Global mean is: " << globalMean << " T bins: " << K << " . Now creating shards." << std::endl;
    }
    else {
        globalMean2 /= Le;
        logstream(LOG_INFO) << "Global mean is: " << globalMean2 << " T bins: " << K << " . Now creating shards." << std::endl;
    }
    write_global_mean(base_filename, type);

    sharderobj.end_preprocessing();

    fclose(f);
    logstream(LOG_INFO) << "Now creating shards." << std::endl;

    // Shard with a specified number of shards, or determine automatically if not defined
    nshards = sharderobj.execute_sharding(get_option_string("nshards", "auto"));

    return nshards;
}

double training_rse(int iteration, graphchi_context &gcontext, bool items = false){
  last_training_rmse = dtraining_rmse;
  dtraining_rmse = 0;
  double ret = 0;
  dtraining_rmse = sum(rmse_vec);
  int old_loss = loss_type;
  if (loss_type == AP)
    loss_type = SQUARE;
  ret = dtraining_rmse;
  std::cout<< std::setw(10) << mytimer.current_time() << ") Iteration: " << std::setw(3) <<iteration<<" Training " << error_names[loss_type] << ":"<< std::setw(10)<< dtraining_rmse;
  loss_type = old_loss;

  return ret;
}
